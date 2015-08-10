#!/bin/bash
# this is workaround as maven aspect compiler was picking up gitignore 
mv diksha-common/src/main/java/org/diksha/common/utils/.gitignore temp_gitignore
mvn clean 
mvn package
mv temp_gitignore diksha-common/src/main/java/org/diksha/common/utils/.gitignore

