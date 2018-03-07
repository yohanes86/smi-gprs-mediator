package com.emobile.jets.mayapada.smi.http;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.nio.protocol.BasicAsyncRequestConsumer;
import org.apache.http.nio.protocol.BasicAsyncResponseProducer;
import org.apache.http.nio.protocol.HttpAsyncExchange;
import org.apache.http.nio.protocol.HttpAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestHandler;
import org.apache.http.protocol.HttpContext;

import com.emobile.jets.mayapada.smi.BaseSmiReceiverHandler;

public abstract class BaseHttpReceiverHandler extends BaseSmiReceiverHandler 
		implements HttpAsyncRequestHandler<HttpRequest> {
	
	protected abstract void handleInternal(final HttpRequest request, 
			final HttpResponse response, final HttpContext context);
	
	@Override
	public void handle(final HttpRequest request, final HttpAsyncExchange exchange,
            final HttpContext context) throws HttpException, IOException {
        HttpResponse response = exchange.getResponse();
        handleInternal(request, response, context);
        exchange.submitResponse(new BasicAsyncResponseProducer(response));
    }

	@Override
	public HttpAsyncRequestConsumer<HttpRequest> processRequest(
            final HttpRequest request, final HttpContext context) {
        // Buffer request content in memory for simplicity
        return new BasicAsyncRequestConsumer();
    }

	

}
