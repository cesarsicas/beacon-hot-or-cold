# Beacon Hot or Cold ?
## (English)
Android app that scan beacons (BLE) and show their proximity through an
annoying sound.

 Technologies used on this project:
* Android
* Java

The scan classes were based on [this](https://github.com/google/eddystone/tree/master/tools/eddystone-validator) example from Google (Thank you guys !)

Developed for study proposes. If you are looking for a lib to use on your project, check [this one](http://altbeacon.github.io/android-beacon-library/) 

## Testing
You will need an Eddystone Beacon or you can simulate one using another Android Phone. 
I recommend the Beacon Toy App for this simulation, you can download it on Play Store.

So, just create one Eddystone-UID. On beacon settings you can generate Namespace and random Instance.

Finally, after compile this app on second phone, the beacon should be detected by scanner and included on the list. 
You need to turn on your bluetooth And location to use this app.  



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

Depois de instalar o Beacon Toy, crie um Eddystone-UID. 
Nas configurações do beacon você pode escolher um namespace aleatório.

Finalmente, após compilar esse aplicativo em um segundo Android, o beacon criado deve ser identificado e colocado na lista.
Para utilizar o aplicativo você precisa deixar habilitado Blueetooth e Geolocalização !

>Twitter @cesarsicas
