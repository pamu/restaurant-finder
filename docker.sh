rm -rf svc
sbt dist
unzip -d svc target/universal/restaurant-finder-0.0.1.zip
mv svc/*/* svc/
rm svc/bin/*.bat
mv svc/bin/* svc/bin/start
docker build -t restaurant-finder .
docker run -it -p 9000:9000 -p 9443:9443 --rm restaurant-finder
