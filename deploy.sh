#!/bin/bash
set -e -u

function log() {
	echo -e "\n[DEPLOYMENT] $1\n"
}

# print help
if [[ ($# -gt 0) && (("$1" == "-h") || ("$1" == "--help")) ]]; then
  echo "Usage: 		`basename $0` NEXT_VERSION"
  echo "or just:	`basename $0`";
  exit 0
fi

LATEST_RELEASE_VERSION=`git describe --abbrev=0 --tags`


log "starting deployment chain ..."

if [[ "$#" -ne 1 ]]; then
	log "Incrementing latest release version";
	[[ "$LATEST_RELEASE_VERSION" =~ v(.*[^0-9])([0-9]+)$ ]] && NEXT_VERSION="${BASH_REMATCH[1]}$((${BASH_REMATCH[2]} + 1))";
else
	NEXT_VERSION=$1;
fi

[[ "$NEXT_VERSION" =~ (.*[^0-9])([0-9]+)$ ]] && NEXT_DEVELOPMENT_VERSION="${BASH_REMATCH[1]}$((${BASH_REMATCH[2]} + 1))-SNAPSHOT";

# debugging
# log $NEXT_VERSION
# log $NEXT_DEVELOPMENT_VERSION

log "Increasing version number to $NEXT_VERSION"
mvn org.codehaus.mojo:versions-maven-plugin:2.0:set -DgenerateBackupPoms=false -DnewVersion=$NEXT_VERSION

log "Building the release"
mvn clean install

log "commiting the new version $NEXT_VERSION"
git commit -a -m "pushes to release version $NEXT_VERSION"

# log "Deploying to server ..."
# ansible-playbook src/main/deployment/deploy-seasupport.yml

log "creating tag v$NEXT_VERSION"
git tag -a v$NEXT_VERSION -m "`curl -s http://whatthecommit.com/index.txt`"

log "Increasing version number to $NEXT_DEVELOPMENT_VERSION"
mvn org.codehaus.mojo:versions-maven-plugin:2.0:set -DgenerateBackupPoms=false -DnewVersion=$NEXT_DEVELOPMENT_VERSION

log "commiting next development version $NEXT_DEVELOPMENT_VERSION"
git commit -a -m "pushes to development version $NEXT_DEVELOPMENT_VERSION"

log "pushing master + tag to remote"
git push origin tag v$NEXT_VERSION && git push origin

log "finished deployment chain :)"
