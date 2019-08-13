package com.vibe.integracao.cherwell;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@RestController
public class XmlController{

	HttpHeaders headers = new HttpHeaders();
	HttpEntity<String> request;
	RestTemplate restTemplate;

	@GetMapping("")
	public ResponseEntity<?> getAll(){
		restTemplate = new RestTemplate();

		String xmlString = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:cher=\"http://cherwellsoftware.com\">\r\n"
				+ "   <soapenv:Header/>\r\n" 
				+ "    	<soapenv:Body>\r\n" 
				+ "      <cher:QueryByFieldValue>\r\n"
				+ "         <!--Optional:-->\r\n"
				+ "         <cher:busObNameOrId>9355d5ed41e384ff345b014b6cb1c6e748594aea5b</cher:busObNameOrId>\r\n"
				+ "         <!--Optional:-->\r\n" 
				+ "         <cher:fieldNameOrId>OwnerTeamID</cher:fieldNameOrId>\r\n"
				+ "         <!--Optional:-->\r\n"
				+ "         <cher:value>941904aca8372f66e6b8b74d9c80797ffeac11167a</cher:value>\r\n"
				+ "      </cher:QueryByFieldValue>\r\n"  
				+ "   </soapenv:Body>\r\n" + "</soapenv:Envelope>";

		headers.set("Content-Type", "text/xml; charset=utf-8");

		// usuário:cherwell\Cherwell_Vibe , senha:vibe123
//        headers.set("Authorization", "Basic Y2hlcndlbGxcQ2hlcndlbGxfVmliZTp2aWJlMTIz");
		headers.setBasicAuth("cherwell\\Cherwell_Vibe", "vibe123");
		request = new HttpEntity<String>(xmlString, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("https://10.0.1.38/CherwellService/api.asmx",
				request, String.class);
		System.out.println(response.getBody());

		return ResponseEntity.ok().body(response.getBody());
	}
	
	public RestTemplate getRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
	    TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
	        @Override
	        public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
	            return true;
	        }
	    };
	    SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
	    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
	    CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
	    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
	    requestFactory.setHttpClient(httpClient);
	    RestTemplate restTemplate = new RestTemplate(requestFactory);
	    return restTemplate;
	}

	
}
