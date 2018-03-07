package com.emobile.jets.mayapada.smi.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.nio.DefaultHttpServerIODispatch;
import org.apache.http.impl.nio.DefaultNHttpServerConnection;
import org.apache.http.impl.nio.DefaultNHttpServerConnectionFactory;
import org.apache.http.impl.nio.SSLNHttpServerConnectionFactory;
import org.apache.http.impl.nio.reactor.DefaultListeningIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.NHttpConnectionFactory;
import org.apache.http.nio.NHttpServerConnection;
import org.apache.http.nio.protocol.HttpAsyncRequestHandler;
import org.apache.http.nio.protocol.HttpAsyncRequestHandlerRegistry;
import org.apache.http.nio.protocol.HttpAsyncService;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.nio.reactor.ListeningIOReactor;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpReceiverServer {
	private final static Logger LOG = LoggerFactory.getLogger(HttpReceiverServer.class);
	
	private String id;
	
	private int port;
	
	private String keyStoreName;
	
	private String keyStorePass;
	
	private int socketTimeout = 30000; // def: 30s
	
	private ListeningIOReactor ioReactor;

	private IOEventDispatch ioEventDispatch;
	
	private HttpAsyncRequestHandler<HttpRequest> requestHandler;
	
	private int workerCount = 20;
	
	/**
	 * @throws IOReactorException
	 */
	private void createReactor() throws Exception {
		// HTTP parameters for the server
        HttpParams params = new SyncBasicHttpParams();
		params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, socketTimeout)
			.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
			.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
			.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
			.setBooleanParameter(CoreConnectionPNames.SO_KEEPALIVE, false)
			.setParameter(CoreProtocolPNames.ORIGIN_SERVER, "HttpComponents/1.1");
		
		// Create HTTP protocol processing chain
        HttpProcessor httpproc = new ImmutableHttpProcessor(new HttpResponseInterceptor[] {
                // Use standard server-side protocol interceptors
                new ResponseDate(),
                new ResponseServer(),
                new ResponseContent(),
                new ResponseConnControl()
        });
        
		// Create request handler registry
        HttpAsyncRequestHandlerRegistry reqistry = new HttpAsyncRequestHandlerRegistry();
        reqistry.register("*", requestHandler);
		
        // Create server-side HTTP protocol handler
        HttpAsyncService protocolHandler = new HttpAsyncService(
                httpproc, new DefaultConnectionReuseStrategy(), reqistry, params) {

            @Override
            public void connected(final NHttpServerConnection conn) {
            	LOG.debug(conn + ": Connection open");
                super.connected(conn);
            }

            @Override
            public void closed(final NHttpServerConnection conn) {
            	LOG.debug(conn + ": Connection closed");
                super.closed(conn);
            }

        };
        
	     // Create HTTP connection factory
		NHttpConnectionFactory<DefaultNHttpServerConnection> connFactory;
		if (keyStoreName != null && keyStorePass != null) {
			ClassLoader cl = this.getClass().getClassLoader();
		    URL url = cl.getResource(keyStoreName);  //"test.keystore");
		    KeyStore keystore  = KeyStore.getInstance("jks");
		    keystore.load(url.openStream(), keyStorePass.toCharArray()); 
		    KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(
		            KeyManagerFactory.getDefaultAlgorithm());
		    kmfactory.init(keystore, keyStorePass.toCharArray()); 
		    KeyManager[] keymanagers = kmfactory.getKeyManagers(); 
		    SSLContext sslContext = SSLContext.getInstance("TLS");
		    sslContext.init(keymanagers, null, null);
		    connFactory = new SSLNHttpServerConnectionFactory(sslContext, null, params);
		} else {
			connFactory = new DefaultNHttpServerConnectionFactory(params);
		}

		// Create server-side I/O event dispatch
        ioEventDispatch = new DefaultHttpServerIODispatch(protocolHandler, connFactory);
        LOG.debug("ioEventDispatch Apply..");
        
        IOReactorConfig reactorConfig = new IOReactorConfig();
        reactorConfig.setIoThreadCount(workerCount);
        
        // Create server-side I/O reactor
        ioReactor = new DefaultListeningIOReactor(reactorConfig);
        LOG.debug("IoReactor Apply..");
        LOG.debug("IoThreadCount: " + reactorConfig.getIoThreadCount());
	}

	public void start() {
		try {
			createReactor();
			ioReactor.listen(new InetSocketAddress(port));
			new Thread(new Runnable() {
				public void run() {
					try {
						ioReactor.execute(ioEventDispatch);
					} catch (IOException e) {
						LOG.error("I/O Exception when starting server on port " + port, e);
					}
				}
			}).start();

		} catch (Exception e) {
			LOG.error("Unknown error", e);
		}
		
		LOG.info("Channel HTTP Server [{}] is listening on Port: {}", 
				new String[] { id, "" + port });
	}

	public void stop() {
		LOG.info("[{}] Channel HTTP Server is shutting down", new String[] {id});
		try {
			ioReactor.shutdown();
		} catch (IOException e) {
			LOG.error("I/O Exception when stopping server", e);
		}
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setRequestHandler(HttpAsyncRequestHandler<HttpRequest> requestHandler) {
		this.requestHandler = requestHandler;
	}

	public void setKeyStoreName(String keyStoreName) {
		this.keyStoreName = keyStoreName;
	}

	public void setKeyStorePass(String keyStorePass) {
		this.keyStorePass = keyStorePass;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public void setWorkerCount(int workerCount) {
		this.workerCount = workerCount;
	}
}
