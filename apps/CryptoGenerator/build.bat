dir
echo y|rmdir /s target
dir
call mvn clean install -DskipTests
call docker build --tag cryptogenerator:"%1" .
bash -c "minikube image load cryptogenerator:%1 -p dev-cluster"
bash -c "minikube image ls -p dev-cluster | grep cryptogenerator:%1"
bash -c "kubectl get pods -n generation-service"
bash -c "kubectl set image deployment/crypto-generator crypto-generator=cryptogenerator:%1 -n generation-service"
timeout 10
bash -c "kubectl get pods -n generation-service"
FOR /F "tokens=*" %%g IN ('bash -c "kubectl get pods -o json -n generation-service | jq -r '.items[0].metadata.name'"') do (SET POD_NAME=%%g)
echo "%POD_NAME%"
bash -c "kubectl get pods -n generation-service"
bash -c "kubectl port-forward %POD_NAME% 8080:8080 -n generation-service"