# Beacon Hot or Cold ?
## (English)
Android app that scan beacons (BLe) and show their proximity through an
annoying sound.

Used Techs:
* Android
* Java

The scan classes were based on this [example](https://github.com/google/eddystone/tree/master/tools/eddystone-validator) from Google (Thank you guys !)

Developed for study proposes. If you are looking for a lib to use on your project, check [this one](http://altbeacon.github.io/android-beacon-library/) 

###Testing
You will need an Eddystone Beacon or you can simulate one using another Android Phone. 
I recommend the app Beacon Toy for simulate, you can download it on Play Store.

So, just create one Eddystone-UID. On beacon settings you can generate Namespace and random Instance.

Then, after compile this app on second phone, the beacon should be detected by scanner and included on the list. 
You need to turn your bluetooth And location to use this app.  



## (Português)
Aplicativo Android que Escaneia Beacons (BLE) e indica proximidade a partir de um som irritante.

Tecnologias Utilizadas
* Android
* Java

As classes de Scan foram baseadas nesse [Exemplo](https://github.com/google/eddystone/tree/master/tools/eddystone-validator) da Google (Valeu galera !)

Desenvolvido para fins de estudo. Se você está procurando alguma lib para usar em seu projeto, você deveria olhar [essa](http://altbeacon.github.io/android-beacon-library/)


###Testando
Você precisa ter um Beacon Eddystone ou você pode simular um usando outro celular Android.
Pra isso basta utilizar o app Beacon Toy, que pode ser baixado na Play Store.

Depois de intalar o Beacon Toy, crie um Eddystone-UID. 
Nas configurações do beacon você pode escolher um namespace aleatório.

Finalmente, após compilar esse aplicativo em um segundo Android, o beacon criado deve ser identificado e colocado na lista.
Para utilizar o aplicativo você precisa deixar habilitaod Blueetooth e Geolocalização !

>Twitter @cesarsicas
