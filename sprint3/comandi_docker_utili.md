### per entrare dentro un container running
docker exec -it arch3 bash

utile se vuoi modificare i file di configurazione prolog



### per filtrare le stampe dei componenti che non interessano
Avvia i container in background:

    docker compose up -d

Visualizza solo i log dei servizi che ti interessano:

    docker compose -f arch3-rpi.yaml logs -f arch3