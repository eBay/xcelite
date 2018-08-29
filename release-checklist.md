### Release-Checklist:
    - create release tag
    - mvn release:prepare
    - mvn release:perform
    - git push --tags
    - git push origin master

### Release-documentation:
- https://medium.com/@nmauti/publishing-a-project-on-maven-central-8106393db2c3
- https://dzone.com/articles/publish-your-artifacts-to-maven-central


### .m2/settings.xml has to look like this:
        <?xml version="1.0" encoding="UTF-8"?>
        <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
                  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">


            <servers>
                <server>
                    <id>ossrh</id>
                    <username>Xcelite</username>
                    <password> ### sonatype-jira-password ### </password>
                </server>
            </servers>

            <profiles>
                <profile>
                <id>ossrh</id>
                <activation>
                    <activeByDefault>true</activeByDefault>
                </activation>
                <properties>
                        <gpg.passphrase> ###pgp-password ###</gpg.passphrase>
                </properties>
                </profile>
            </profiles>
        </settings>