# OpenTracing Spring Boot Istio

This repository contains an example Spring Boot app instrumented with OpenTracing
and deployed on Kubernetes 

## Run

1. Install minikube, [Istio](https://istio.io/docs/setup/kubernetes/quick-start.html) and 
[Jaeger on Istio](https://istio.io/docs/tasks/telemetry/distributed-tracing.html).

2. After Istio is up and running build and deploy the app:
```bash
eval $(minikube docker-env) 
./mvnw clean install && docker build -t spring-boot:latest .
kubectl apply -f <(istioctl kube-inject -f app.yml)
# Optional to delete the app -> kubectl delete all,ing -l sb-demo
```

3. Create requests:
```bash
export GATEWAY_URL=$(kubectl get po -n istio-system -l istio=ingress -o 'jsonpath={.items[0].status.hostIP}'):$(kubectl get svc istio-ingress -n istio-system -o 'jsonpath={.spec.ports[0].nodePort}')
curl $GATEWAY_URL/chaining
```

4. Go to Jaeger UI and see the traces
```bash
minikube service jaeger-query --url -n istio-system
```
