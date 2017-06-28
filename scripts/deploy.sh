#!/usr/bin/env bash

BRANCH=$1
VERSION=$2

case "${BRANCH}" in
  develop)
    env="STAGE"
    tag="latest-staging"
    ;;
  master)
    env="RELEASE"
    tag="latest"
    echo "Error: Production not setup yet"
    exit -1
    ;;
  *)
    echo "Error: Branch not allowed:" ${BRANCH}
    exit -1
    ;;
esac

username=`printenv DOCKER_USERNAME_${env}`
password=`printenv DOCKER_PASSWORD_${env}`
registry=`printenv DOCKER_REGISTRY_${env}`
repo="${registry}/quantum"
image=${repo}:${VERSION}

docker login -u=${username} -p=${password} ${registry} && \
docker build -t ${image} . && \
docker tag ${image} ${repo}:${tag} && \
docker push ${image} && \
kubectl set image deployment/quantum-deployment quantum=${image} && \
echo Deployed ${image} to kubernetes cluster