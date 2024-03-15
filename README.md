## Pocket-News   

## Branches

- **main:** Jetpack Compose using Dagger Hilt
- **xml-dagger-hilt:** XML using Dagger Hilt
- **xml-dagger-2:** XML using Dagger 2

## Migrate XML Project to Jetpack Compose

- **Update Android Gradle Plugin and Kotlin Plugin:** Using the latest versions of the Android Gradle Plugin and Kotlin Plugin.
- **Add Compose Dependencies:** build.gradle (module-level)
- **Set Up Compose Application:** Create a new @Composable function
- **Replace XML Layouts with Compose Code:** For example, if you had an XML layout with a TextView, replace it with a Compose Text element.
- **Adopt Compose Components:** Replace XML-based UI components with their Compose equivalents.
- **Integrate Compose Navigation:** Migrate from XML-based navigation to Compose Navigation.
- **Migrate UI Logic:** Update UI logic to use Compose's state management.
- **Migrate Resources:** Migrate string resources, colors, drawable.
- **Update Gradle Plugin Versions:** Update your Gradle dependencies accordingly.
- **Testing:** Write tests for your Compose UI using the Compose testing library.
- **Documentation and Learning:** - Refer to the official Jetpack Compose documentation and samples. Learn about Compose concepts like Composables, state management, and navigation.

## Migrate Dagger2 Project to Dagger-Hilt

- **Add Hilt Dependencies:** build.gradle (module-level)
- **Apply Hilt Gradle Plugin:** build.gradle (apply plugin: 'dagger.hilt.android.plugin').
- **Update Dagger Annotations:** Replace Dagger annotations with Hilt annotations where needed, For example, replace @Component with @HiltComponent.
- **Add Hilt Android Application:** Replace Dagger's DaggerAppComponent with Hilt's Hilt_AppComponent.
- **Annotate Android Application Class:** Annotate your Application class with @HiltAndroidApp.
- **Replace Dagger Android Modules:** Replace Dagger Android modules with Hilt Android entry points.
- **Update Injection in Activities/Fragments:** Replace Dagger with Hilt for activity or fragment injection.
- **Update ViewModel Injection:** Replace Dagger ViewModelFactory and ViewModelKey with Hilt's HiltViewModel and @ViewModelInject.
- **Update Dagger Android Testing Components:** Replace DaggerMyTestComponent with HiltTestApplicationComponent.

  ## Major Highlights

- **Jetpack Compose** for modern UI
- **Offline caching** with a **single source of truth**
- **MVVM architecture** for a clean and scalable codebase
- **Kotlin** and **Kotlin DSL**
- **Dagger Hilt** for efficient dependency injection.
- **Retrofit** for seamless networking
- **Room DB** for local storage of news articles
- **Coroutines** and **Flow** for asynchronous programming
- **StatFlow** for streamlined state management
- **Pagination** to efficiently load and display news articles
- **Unit tests** and **UI tests** for robust code coverage
- **Instant search** for quick access to relevant news
- **Navigation** for smooth transitions between screens
- **WorkManager** for periodic news fetching
- **Notification** for alerting about latest news
- **Coil** for efficient image loading


- ## Feature implemented:
- NewsApp
- Instant search using Flow operators
    - Debounce
    - Filter
    - DistinctUntilChanged
    - FlatMapLatest
- Offline news
- Pagination
- Unit Test
    - Mockito
    - Turbine https://github.com/cashapp/turbine
    - Espresso
- TopHeadlines of the news
- Country-wise news
- Multiple Languages selection-wise news
- Multiple sources wise news

## Dependency Use

- Jetpack Compose for UI: Modern UI toolkit for building native Android UIs
- Coil for Image Loading: Efficiently loads and caches images
- Retrofit for Networking: A type-safe HTTP client for smooth network requests
- Dagger Hilt for Dependency Injection: Simplifies dependency injection
- Room for Database: A SQLite object mapping library for local data storage
- Paging Compose for Pagination: Simplifies the implementation of paginated lists
- Mockito, JUnit, Turbine for Testing: Ensures the reliability of the application

##  Dependency Used:

- UI
```
implementation ("androidx.compose.ui:ui")
implementation ("androidx.compose.ui:ui-graphics")
implementation ("androidx.compose.ui:ui-tooling-preview")
implementation ("androidx.compose.foundation:foundation")
implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
implementation ("androidx.activity:activity-compose:1.8.2")
implementation(platform("androidx.compose:compose-bom:2023.08.00"))
```

- Material3
```
implementation ("androidx.compose.material3:material3:1.1.2")
```

- Navigation
```
implementation ("androidx.hilt:hilt-navigation-compose:1.1.0")
implementation ("androidx.navigation:navigation-compose:2.7.6")
```

- Coil for image loading
```
implementation ("io.coil-kt:coil-compose:2.4.0")
```

- Retrofit for networking
```
 implementation("com.squareup.retrofit2:retrofit:2.9.0")
 implementation("com.squareup.retrofit2:converter-gson:2.9.0")
 implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
```
- Paging library
```
implementation 'androidx.paging:paging-runtime-ktx:3.2.1'
implementation 'androidx.paging:paging-compose:3.2.1'
```

- Dagger hilt for dependency injection
```
 implementation("com.google.dagger:hilt-android:2.44")
 kapt("com.google.dagger:hilt-android-compiler:2.44")
```

- For webView browser
```
implementation 'androidx.browser:browser:1.4.0'
```

- Room database
```
implementation ("androidx.room:room-runtime:2.5.0")
kapt ("androidx.room:room-compiler:2.6.1")
// optional - Kotlin Extensions and Coroutines support for Room
implementation ("androidx.room:room-ktx:2.5.0")
```

- WorkManager
```
 implementation("androidx.work:work-runtime-ktx:2.9.0")
 implementation("androidx.hilt:hilt-work:1.1.0")
 kapt("androidx.hilt:hilt-compiler:1.1.0")
```
- Local Unit test
```
testImplementation 'junit:junit:4.13.2'
testImplementation "org.mockito:mockito-core:5.3.1"
testImplementation 'androidx.arch.core:core-testing:2.2.0'
testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3'
testImplementation 'app.cash.turbine:turbine:0.12.1'
```
- UI Test
```
androidTestImplementation 'androidx.test.ext:junit:1.1.5'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
androidTestImplementation platform('androidx.compose:compose-bom:2023.03.00')
androidTestImplementation 'androidx.compose.ui:ui-test-junit4'
androidTestImplementation 'androidx.navigation:navigation-testing:2.6.0'
debugImplementation 'androidx.compose.ui:ui-tooling'
debugImplementation 'androidx.compose.ui:ui-test-manifest'
```

## The Complete Project Folder Structure

```
└── com
    └── example
        └── pocketnews
            ├── NewsApplication.kt
            ├── data
            │   ├── api
            │   │   ├── ApiKeyInterceptor.kt
            │   │   └── NetworkService.kt
            │   ├── local
            │   │   ├── AppDatabaseService.kt
            │   │   ├── DatabaseService.kt
            │   │   ├── NewsAppDatabase.kt
            │   │   ├── dao
            │   │   │   ├── SourceDao.kt
            │   │   │   └── TopHeadlinesDao.kt
            │   │   └── entity
            │   │       ├── Article.kt
            │   │       ├── NewsSources.kt
            │   │       └── Source.kt
            │   ├── model
            │   │   ├── Country.kt
            │   │   ├── Language.kt
            │   │   ├── newssources
            │   │   │   ├── APINewsSource.kt
            │   │   │   └── NewsSourcesResponse.kt
            │   │   └── topheadlines
            │   │       ├── ApiArticle.kt
            │   │       ├── ApiSource.kt
            │   │       └── TopHeadlinesResponse.kt
            │   └── repository
            │       ├── CountryListRepository.kt
            │       ├── LanguageListRepository.kt
            │       ├── NewsRepository.kt
            │       ├── NewsSourceRepository.kt
            │       ├── OfflineTopHeadlineRepository.kt
            │       ├── PaginationTopHeadlineRepository.kt
            │       ├── SearchRepository.kt
            │       ├── TopHeadlinePagingSource.kt
            │       └── TopHeadlineRepository.kt
            ├── di
            │   ├── module
            │   │   └── ApplicationModule.kt
            │   └── qualifier.kt
            ├── ui
            │   ├── MainActivity.kt
            │   ├── base
            │   │   ├── CommonUI.kt
            │   │   ├── Navigation.kt
            │   │   └── UiState.kt
            │   ├── country
            │   │   ├── CountryListScreen.kt
            │   │   └── CountryListViewModel.kt
            │   ├── home
            │   │   └── HomeScreenRoute.kt
            │   ├── language
            │   │   ├── LanguageListScreen.kt
            │   │   └── LanguageListViewModel.kt
            │   ├── news
            │   │   ├── NewsListScreen.kt
            │   │   └── NewsListViewModel.kt
            │   ├── offline
            │   │   ├── OffineTopHeadlineScreen.kt
            │   │   └── OfflineTopHeadlineViewModel.kt
            │   ├── pagination
            │   │   ├── PaginationTopHeadlineScreen.kt
            │   │   └── PaginationTopHeadlineViewModel.kt
            │   ├── search
            │   │   ├── SearchScreen.kt
            │   │   └── SearchViewModel.kt
            │   ├── sources
            │   │   ├── NewsSourcesScreen.kt
            │   │   └── NewsSourcesViewModel.kt
            │   ├── theme
            │   │   ├── Color.kt
            │   │   ├── Shape.kt
            │   │   ├── Theme.kt
            │   │   └── Type.kt
            │   └── topheadline
            │       ├── TopHeadlineScreen.kt
            │       └── TopHeadlineViewModel.kt
            ├── utils
            │   ├── AppConstant.kt
            │   ├── DispatcherProvider.kt
            │   ├── NetworkHelper.kt
            │   ├── NetworkHelperImpl.kt
            │   ├── TimeUtil.kt
            │   ├── logger
            │   │   ├── AppLogger.kt
            │   │   └── Logger.kt
            │   └── typealias.kt
            └── worker
                └── FetchTopHeadlinesWorker.kt
```

## Contribute to the project
Feel free to improve or add features to the project. Create an issue or find the pending issue. All pull requests are welcome 😄

## 🚀 About Me
Hi there! My name is Sneha Annigeri, I worked as a Senior Android Developer. Outside of my professional commitments, I actively pursue opportunities to expand my skill set. 
I am deeply passionate about continuous learning and aim to leverage my spare time to enhance my expertise in Android development.
If you have any questions or want to connect, feel free to reach out to me on :

- [LinkedIn](https://www.linkedin.com/in/sneha-annigeri/)
- [GitHub](https://github.com/SnehaAwate)
