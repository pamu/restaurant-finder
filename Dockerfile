FROM openjdk:8-jre
COPY svc /svc
EXPOSE 9000 9443
CMD /svc/bin/start -Dhttps.port=9443 -Dplay.http.secret.key=2a;woara[bs@9inj5PSrUByesd4zxu6oLuAVQ9]HC`zqOOI>v4f/^tNvl`p7iL;m
