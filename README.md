# SuitePadTask

This repository contain 3 micro apps:

+ *DataSourceApp*: Stores cached data.
+ *HTTPProxyApp*: Intercepts HTTP requests and search for cached data.
+ *MenuApp*: show a WebView with items offered on the menu.

###About SSL connections from MenuApp

If the request is intercepted on a HTTPS request, the WebView client and the remote server will reject the petition because the certificates of each origin so will not be possible create a secure session for connection because the "handshake" will not be successfully done.

We can configure the WebViewClient to accept certificates and use SSLSocket for the connection with proxy. For the connection with the HTTPProxyApp we will need to do a "Tunel Request" over SSL initiating first a secure session with remote server and then making HTTP transaction with MenuApp since are in same subnet that proxy.
