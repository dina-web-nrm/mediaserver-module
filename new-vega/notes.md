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

OBS-> 
1.1 http fungerar hemma utan problem med re-direct !?

2. testa att post till 'https'
- "https://media.nrm.se/mserver/rest/file/vega"
3. Hur fungerar detta då på seqdb-as maskinen ?

4. nästa steg
4.1 implementera klienten på vegadare-sidan
4.2 kika på Java_HEAP i standalone.conf på vegadare-wilfly servern ...
4.3 pusha ny vegadare-image till docker-hub ...