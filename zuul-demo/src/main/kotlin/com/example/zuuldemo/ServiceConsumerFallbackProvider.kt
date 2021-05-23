package com.example.zuuldemo

import com.netflix.zuul.context.RequestContext
import org.springframework.stereotype.Component
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import java.io.ByteArrayInputStream
import java.io.InputStream


/**
 * .
 * @author yonoel 2021/05/17
 */
@Component
class ServiceConsumerFallbackProvider : FallbackProvider{
    override fun getRoute(): String {
        return "*"
    }

    override fun fallbackResponse(route: String?, cause: Throwable?): ClientHttpResponse {
        return object : ClientHttpResponse{
            override fun getHeaders(): HttpHeaders {
                return HttpHeaders.EMPTY
            }

            override fun getBody(): InputStream {
                if (cause != null){
                    print(cause)
                }
                val ctx = RequestContext.getCurrentContext()
                return ByteArrayInputStream("{err:1}".toByteArray())
            }

            override fun close() {
            }

            override fun getStatusCode(): HttpStatus {
                return HttpStatus.OK
            }

            override fun getRawStatusCode(): Int {
                return this.statusCode.value()
            }

            override fun getStatusText(): String {
                return this.statusCode.reasonPhrase
            }
        }
    }
}