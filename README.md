# An example of progress-centric notifications in Android 16

This demonstrates the following features of progress-centric notifications:

* Creating a basic ongoing notification (including relevant permissions)
* Requesting its promotion (note required `android.permission.POST_PROMOTED_NOTIFICATIONS` in the Manifest)
* Show different progress points
* Displaying different coloured segments
* Setting a tracker icon (scarecrow)
* Showing custom text on the notification chip ("short critical text")

This repo is not designed to be backward compatible -- but it does nonetheless use NotificationCompat so you may well find it works for older Android versions.

This example was written for my talk on empathetic UX at droidcon Berlin 2025.

---

_Need help with notifications, or any other part of your Android app? I'm a Google Developer Expert available as a [freelancer](https://tomcolvin.co.uk) or through my agency [Apptaura](https://apptaura.com)._

MIT license
