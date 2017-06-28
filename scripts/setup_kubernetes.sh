#!/usr/bin/env bash

BRANCH=$1

case "${BRANCH}" in
  develop)
    env="STAGE"
    ;;
  master)
    env="RELEASE"
    echo "Error: Production not setup yet"
    exit -1
    ;;
  *)
    echo "Error: Branch not allowed:" ${BRANCH}
    exit -1
    ;;
esac

cluster_server=`printenv CLUSTER_SERVER_${env}`
cluster_name=`printenv CLUSTER_NAME_${env}`
certificate_authority_data=`printenv CERTIFICATE_AUTHORITY_DATA_${env}`
client_certificate_data=`printenv CLIENT_CERTIFICATE_DATA_${env}`
client_key_data=`printenv CLIENT_KEY_DATA_${env}`

### Install kubernetes
echo "Downloading kubernetes binary..."
curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl
chmod +x ./kubectl
sudo mv ./kubectl /usr/local/bin/kubectl

### Create config
echo "Creating kubernetes configuration..."
mkdir ${HOME}/.kube
cat > ${HOME}/.kube/config << EOF
---
apiVersion: v1
clusters:
  - cluster:
      certificate-authority-data: "${certificate_authority_data}"
      server: "${cluster_server}"
    name: "${cluster_name}"
contexts:
  - context:
      cluster: "${cluster_name}"
      user: "${cluster_name}-admin"
    name: "${cluster_name}"
current-context: "${cluster_name}"
kind: Config
users:
  - name: "${cluster_name}-admin"
    user:
      client-certificate-data: "${client_certificate_data}"
      client-key-data: "${client_key_data}"
EOF