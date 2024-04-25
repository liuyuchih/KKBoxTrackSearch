# KKBOX API Android Demo

This Android demo project demonstrates how to interact with the KKBOX API. It is designed to help developers understand how to:
- Obtain an access token from KKBOX.
- Search for tracks using the KKBOX API.

## Features
- **MVVM Architecture**: Utilizes the Model-View-ViewModel (MVVM) pattern for effective UI data management.
- **Data Layer**: Includes manual dependency injection for robustness.
- **Network Calls**: Made using Retrofit.
- **UI Components**: Uses ViewBinding, ListAdapter, and DiffUtil for efficient list operations.

## Prerequisites
Before you start, ensure you have a functioning Android development environment and understand the basics of Android development and API integration.

## Installation
To run this demo, clone the repository and open it in your preferred Android IDE (e.g., Android Studio).

```bash
https://github.com/liuyuchih/KKBoxTrackSearch.git
cd KKBoxTrackSearch
```

### Configuration
Update the `local.properties` file with your KKBOX credentials:

```properties
KKBOX_ID="YOUR_KKBOX_ID"
KKBOX_SECRET="YOUR_KKBOX_SECRET"
```

This project uses the `secrets-gradle-plugin` to manage sensitive data securely. For more information, visit:
[Secrets Gradle Plugin by Google](https://github.com/google/secrets-gradle-plugin)

## Documentation
For more information about the KKBOX API, refer to the official [KKBOX API Documentation](https://docs-zhtw.kkbox.codes/#overview).

## License
This project is open-sourced under the MIT license. Feel free to fork and modify it as per your needs.
