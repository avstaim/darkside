# Cookies

*Not a HTTP cookies, but Darkside cookies.*

Set of little and usable helpers (cookies) to make life a little bit sweetier.


## Content

### `.concept` package

Offers `Either` and `Optional` monads.

_sample:_
```kotlin
private val workManagerEither: Either<WorkManager, RemoteWorkManager> =
    if (configuration.useRemoteWorkManager) {
        RemoteWorkManager.getInstance(applicationContext).either()
    } else {
        WorkManager.getInstance(applicationContext).either()
    }

override suspend fun scheduleOneTimeSync(
    activity: ComponentActivity,
    existingWorkPolicy: PassportContactsExistingWorkPolicy,
): ScheduleStatus {
    
    workManagerEither.fold(
        onLeft = { workManager ->
            val request = buildOneTimeRequest<PassportContactsWorker>(activity, isRemote = false)
            workManager.beginUniqueWork(WORK_NAME, existingWorkPolicy.oneTime, request).enqueue()
        },
        onRight = { workManager ->
            val request = buildOneTimeRequest<PassportContactsRemoteWorker>(activity, isRemote = true)
            workManager.beginUniqueWork(WORK_NAME, existingWorkPolicy.oneTime, request).enqueue()
        },
    )

    return ScheduleStatus.REQUEST_CONFIRMED
}
```


### `.time` package

Offers `CommonTime` inline class to replace `TimeUnit.*.` calls.

_sample:_
```kotlin
var retryDelay = CommonTime(millis = 300)
    set(value) {
        if (value < CommonTime(millis = 10) || value > CommonTime(hours = 24)) {
            KLog.e { "wrong delay value $value" }
            return
        }
        field = value
    }
```

__To be continued...__