🚀 Java Spring Boot Notification Service (Maker-Checker Flow)
A robust and scalable notification system built with Java and Spring Boot, implementing a Maker-Checker workflow. This service ensures that every administrative action (like slab fee changes) is verified and communicated asynchronously using Retrofit 2.

🛠️ Technologies Used
Java 17: Core programming language.

Spring Boot: For building the microservice architecture.

Retrofit 2 & OkHttp: For handling type-safe REST API calls to the Alert Service.

GSON: For JSON serialization and deserialization.

Lombok: To reduce boilerplate code (Getters/Setters).

Multithreading: Implemented background threads for non-blocking email delivery.

🌟 Key Features
1. Maker-Checker Logic
Maker: Submits a request for a change (e.g., fee slab update).

Checker: Reviews the request and either Approves or Rejects it.

Automatic Live Sync: Upon approval, the system automatically updates the live production tables.

2. Asynchronous Email Notifications
The system sends real-time email notifications at every stage:

On Submission: Notifies all relevant Checkers.

On Approval/Rejection: Notifies the original Maker.

Background Threads: Email triggering is decoupled from the main business logic using threads, ensuring zero latency for the end-user.

3. Retrofit Integration
Utilized a centralized RetrofitManager to manage API connections.

Implemented custom timeouts and error handling for external service dependencies.

📂 Project Structure Highlights
EmailUtils.java: A utility class that prepares email templates and manages background execution.

RetrofitManager.java: A singleton manager for configuring Retrofit and Alert Service base URLs.

SlabMakerCheckerServiceImpl.java: The core business logic that handles bulk approvals and triggers notifications.

⚙️ How It Works (Conceptual)
The user sends a request via Postman to the service endpoint.

The service validates the request and saves a record in the MakerCheckerOzSlab table.

The EmailUtils is called, which starts a new thread.

Retrofit sends a POST request to the Alert Service with the email data.

Success logs are printed on the console upon a statusCode: 0 response.

💡 What I Learned
Managing Static vs. Non-Static contexts in Spring Boot.

Handling Bulk Actions in a loop while maintaining notification integrity.

Best practices for integrating third-party APIs in a microservices environment.
