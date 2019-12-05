2019-12-05

2 ändringar i docker-media.
1. HEAP i standalone.conf
2. jboss-cli.sh -> max-post-size.

Lokalt.
1. för att kunna köra POST med klienten, och posta till 'http'  
- "http://media.nrm.se/mserver/rest/file/vega"
- ingen  re-direct , response till klienten är 301
- ändrar :  i proxy (tar bort /certs)
- fungerar

2. testa att post till 'https'
- "https://media.nrm.se/mserver/rest/file/vega"
3. Hur fungerar detta då på seqdb-as maskinen ?