## Disposability and Scaling

I scaled Catalog Service to two replicas and deleted one 
of the running pods. Kubernetes automatically created a 
replacement pod to maintain the desired number of replicas. 
The replacement pod did not become READY 1/1 immediately. 
It only became ready after the readiness probe succeeded.