
dpkg-query -W -f='${binary:Package}\n' | grep -E -e '^(ia32-)?(sun|oracle)-java' -e '^openjdk-' -e '^icedtea' -e '^(default|gcj)-j(re|dk)' -e '^gcj-(.*)-j(re|dk)' -e '^java-common' | xargs sudo apt-get -y remove

sudo apt-get -y autoremove

dpkg -l | grep ^rc | awk '{print($2)}' | xargs sudo apt-get -y purge

sudo bash -c 'ls -d /home/*/.java' | xargs sudo rm -rf

sudo rm -rf /usr/lib/jvm/*

for g in ControlPanel java java_vm javaws jcontrol jexec keytool mozilla-javaplugin.so orbd pack200 policytool rmid rmiregistry servertool tnameserv unpack200 appletviewer apt extcheck HtmlConverter idlj jar jarsigner javac javadoc javah javap jconsole jdb jhat jinfo jmap jps jrunscript jsadebugd jstack jstat jstatd native2ascii rmic schemagen serialver wsgen wsimport xjc xulrunner-1.9-javaplugin.so; do sudo update-alternatives --remove-all $g; done

sudo updatedb
sudo locate -b '\pack200'





mkdir /usr/java
sudo tar xvzf jdk-7u80-linux-i586.tar.gz -C /usr/java
JAVA_HOME=/usr/java/jdk1.7.0_80/

echo "JAVA_HOME=/usr/java/jdk1.8.0_05/" >> /etc/environment
sudo update-alternatives --install /usr/bin/java java ${JAVA_HOME%*/}/bin/java 20000
sudo update-alternatives --install /usr/bin/javac javac ${JAVA_HOME%*/}/bin/javac 20000

update-alternatives --config java


java -version


sudo dpkg --add-architecture i386
sudo apt-get update
sudo apt-get install libc6-i386
