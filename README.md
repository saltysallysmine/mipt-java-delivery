# Delivery

**This is educational project for MIPT Java course.**

Execute tests if you want to see what it can do. Users send POST-requests from different cities, **MainController** catches them and redirect to right RabbitMQ queues.

I am keeping ahead of forward compatibility, this is why I created the **City class** to which new cities can easily be added.

## Running project tests
Firstly, run RabbitMQ at the root of the project.

```bash
sudo docker compose up
# or
docker compose up
```

Then build project and execute **MainControllerTest** class.

---

Developed by *Bergman Valery*.
