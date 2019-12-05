2019-12-05

2 ändringar i docker-media.
1. HEAP i standalone.conf
2. jboss-cli.sh -> max-post-size.

Lokalt.
1. för att kunna köra POST med klienten, ändring i proxy (tar bort /certs)
- nu blir det ingen re-direct
2. Hur fungerar detta då på seqdb-as maskinen ?