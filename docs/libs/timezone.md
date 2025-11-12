# `timezone`

A library that provides IANA timezone constants for use in Jaiva programs.

## `tz_getAll <- `_**`[]`**_

The complete list of IANA format timezone constants.

**Example:**

```jaiva
tsea "jaiva/timezone"!

@ Get all timezone constants
maak list <- tz_getAll!
@ Print everything with Etc prefix
colonize item with list ->
    if (item ? "Etc") ->
        khuluma(item)!
    <~
<~
```

## Constants

| Variable Name                     | IANA Constant Value              | Link                                                  |
|-----------------------------------|----------------------------------|-------------------------------------------------------|
| TZ_AfricaAbidjan                  | Africa/Abidjan                   | [Link](#tz_africaabidjan---_string_)                  |
| TZ_AfricaAccra                    | Africa/Accra                     | [Link](#tz_africaaccra---_string_)                    |
| TZ_AfricaAddisAbaba               | Africa/Addis_Ababa               | [Link](#tz_africaaddisababa---_string_)               |
| TZ_AfricaAlgiers                  | Africa/Algiers                   | [Link](#tz_africaalgiers---_string_)                  |
| TZ_AfricaAsmara                   | Africa/Asmara                    | [Link](#tz_africaasmara---_string_)                   |
| TZ_AfricaAsmera                   | Africa/Asmera                    | [Link](#tz_africaasmera---_string_)                   |
| TZ_AfricaBamako                   | Africa/Bamako                    | [Link](#tz_africabamako---_string_)                   |
| TZ_AfricaBangui                   | Africa/Bangui                    | [Link](#tz_africabangui---_string_)                   |
| TZ_AfricaBanjul                   | Africa/Banjul                    | [Link](#tz_africabanjul---_string_)                   |
| TZ_AfricaBissau                   | Africa/Bissau                    | [Link](#tz_africabissau---_string_)                   |
| TZ_AfricaBlantyre                 | Africa/Blantyre                  | [Link](#tz_africablantyre---_string_)                 |
| TZ_AfricaBrazzaville              | Africa/Brazzaville               | [Link](#tz_africabrazzaville---_string_)              |
| TZ_AfricaBujumbura                | Africa/Bujumbura                 | [Link](#tz_africabujumbura---_string_)                |
| TZ_AfricaCairo                    | Africa/Cairo                     | [Link](#tz_africacairo---_string_)                    |
| TZ_AfricaCasablanca               | Africa/Casablanca                | [Link](#tz_africacasablanca---_string_)               |
| TZ_AfricaCeuta                    | Africa/Ceuta                     | [Link](#tz_africaceuta---_string_)                    |
| TZ_AfricaConakry                  | Africa/Conakry                   | [Link](#tz_africaconakry---_string_)                  |
| TZ_AfricaDakar                    | Africa/Dakar                     | [Link](#tz_africadakar---_string_)                    |
| TZ_AfricaDaresSalaam              | Africa/Dar_es_Salaam             | [Link](#tz_africadaressalaam---_string_)              |
| TZ_AfricaDjibouti                 | Africa/Djibouti                  | [Link](#tz_africadjibouti---_string_)                 |
| TZ_AfricaDouala                   | Africa/Douala                    | [Link](#tz_africadouala---_string_)                   |
| TZ_AfricaElAaiun                  | Africa/El_Aaiun                  | [Link](#tz_africaelaaiun---_string_)                  |
| TZ_AfricaFreetown                 | Africa/Freetown                  | [Link](#tz_africafreetown---_string_)                 |
| TZ_AfricaGaborone                 | Africa/Gaborone                  | [Link](#tz_africagaborone---_string_)                 |
| TZ_AfricaHarare                   | Africa/Harare                    | [Link](#tz_africaharare---_string_)                   |
| TZ_AfricaJohannesburg             | Africa/Johannesburg              | [Link](#tz_africajohannesburg---_string_)             |
| TZ_AfricaJuba                     | Africa/Juba                      | [Link](#tz_africajuba---_string_)                     |
| TZ_AfricaKampala                  | Africa/Kampala                   | [Link](#tz_africakampala---_string_)                  |
| TZ_AfricaKhartoum                 | Africa/Khartoum                  | [Link](#tz_africakhartoum---_string_)                 |
| TZ_AfricaKigali                   | Africa/Kigali                    | [Link](#tz_africakigali---_string_)                   |
| TZ_AfricaKinshasa                 | Africa/Kinshasa                  | [Link](#tz_africakinshasa---_string_)                 |
| TZ_AfricaLagos                    | Africa/Lagos                     | [Link](#tz_africalagos---_string_)                    |
| TZ_AfricaLibreville               | Africa/Libreville                | [Link](#tz_africalibreville---_string_)               |
| TZ_AfricaLome                     | Africa/Lome                      | [Link](#tz_africalome---_string_)                     |
| TZ_AfricaLuanda                   | Africa/Luanda                    | [Link](#tz_africaluanda---_string_)                   |
| TZ_AfricaLubumbashi               | Africa/Lubumbashi                | [Link](#tz_africalubumbashi---_string_)               |
| TZ_AfricaLusaka                   | Africa/Lusaka                    | [Link](#tz_africalusaka---_string_)                   |
| TZ_AfricaMalabo                   | Africa/Malabo                    | [Link](#tz_africamalabo---_string_)                   |
| TZ_AfricaMaputo                   | Africa/Maputo                    | [Link](#tz_africamaputo---_string_)                   |
| TZ_AfricaMaseru                   | Africa/Maseru                    | [Link](#tz_africamaseru---_string_)                   |
| TZ_AfricaMbabane                  | Africa/Mbabane                   | [Link](#tz_africambabane---_string_)                  |
| TZ_AfricaMogadishu                | Africa/Mogadishu                 | [Link](#tz_africamogadishu---_string_)                |
| TZ_AfricaMonrovia                 | Africa/Monrovia                  | [Link](#tz_africamonrovia---_string_)                 |
| TZ_AfricaNairobi                  | Africa/Nairobi                   | [Link](#tz_africanairobi---_string_)                  |
| TZ_AfricaNdjamena                 | Africa/Ndjamena                  | [Link](#tz_africandjamena---_string_)                 |
| TZ_AfricaNiamey                   | Africa/Niamey                    | [Link](#tz_africaniamey---_string_)                   |
| TZ_AfricaNouakchott               | Africa/Nouakchott                | [Link](#tz_africanouakchott---_string_)               |
| TZ_AfricaOuagadougou              | Africa/Ouagadougou               | [Link](#tz_africaouagadougou---_string_)              |
| TZ_AfricaPorto-Novo               | Africa/Porto-Novo                | [Link](#tz_africaporto-novo---_string_)               |
| TZ_AfricaSaoTome                  | Africa/Sao_Tome                  | [Link](#tz_africasaotome---_string_)                  |
| TZ_AfricaTimbuktu                 | Africa/Timbuktu                  | [Link](#tz_africatimbuktu---_string_)                 |
| TZ_AfricaTripoli                  | Africa/Tripoli                   | [Link](#tz_africatripoli---_string_)                  |
| TZ_AfricaTunis                    | Africa/Tunis                     | [Link](#tz_africatunis---_string_)                    |
| TZ_AfricaWindhoek                 | Africa/Windhoek                  | [Link](#tz_africawindhoek---_string_)                 |
| TZ_AmericaAdak                    | America/Adak                     | [Link](#tz_americaadak---_string_)                    |
| TZ_AmericaAnchorage               | America/Anchorage                | [Link](#tz_americaanchorage---_string_)               |
| TZ_AmericaAnguilla                | America/Anguilla                 | [Link](#tz_americaanguilla---_string_)                |
| TZ_AmericaAntigua                 | America/Antigua                  | [Link](#tz_americaantigua---_string_)                 |
| TZ_AmericaAraguaina               | America/Araguaina                | [Link](#tz_americaaraguaina---_string_)               |
| TZ_AmericaArgentinaBuenosAires    | America/Argentina/Buenos_Aires   | [Link](#tz_americaargentinabuenosaires---_string_)    |
| TZ_AmericaArgentinaCatamarca      | America/Argentina/Catamarca      | [Link](#tz_americaargentinacatamarca---_string_)      |
| TZ_AmericaArgentinaComodRivadavia | America/Argentina/ComodRivadavia | [Link](#tz_americaargentinacomodrivadavia---_string_) |
| TZ_AmericaArgentinaCordoba        | America/Argentina/Cordoba        | [Link](#tz_americaargentinacordoba---_string_)        |
| TZ_AmericaArgentinaJujuy          | America/Argentina/Jujuy          | [Link](#tz_americaargentinajujuy---_string_)          |
| TZ_AmericaArgentinaLaRioja        | America/Argentina/La_Rioja       | [Link](#tz_americaargentinalarioja---_string_)        |
| TZ_AmericaArgentinaMendoza        | America/Argentina/Mendoza        | [Link](#tz_americaargentinamendoza---_string_)        |
| TZ_AmericaArgentinaRioGallegos    | America/Argentina/Rio_Gallegos   | [Link](#tz_americaargentinariogallegos---_string_)    |
| TZ_AmericaArgentinaSalta          | America/Argentina/Salta          | [Link](#tz_americaargentinasalta---_string_)          |
| TZ_AmericaArgentinaSanJuan        | America/Argentina/San_Juan       | [Link](#tz_americaargentinasanjuan---_string_)        |
| TZ_AmericaArgentinaSanLuis        | America/Argentina/San_Luis       | [Link](#tz_americaargentinasanluis---_string_)        |
| TZ_AmericaArgentinaTucuman        | America/Argentina/Tucuman        | [Link](#tz_americaargentinatucuman---_string_)        |
| TZ_AmericaArgentinaUshuaia        | America/Argentina/Ushuaia        | [Link](#tz_americaargentinaushuaia---_string_)        |
| TZ_AmericaAruba                   | America/Aruba                    | [Link](#tz_americaaruba---_string_)                   |
| TZ_AmericaAsuncion                | America/Asuncion                 | [Link](#tz_americaasuncion---_string_)                |
| TZ_AmericaAtikokan                | America/Atikokan                 | [Link](#tz_americaatikokan---_string_)                |
| TZ_AmericaAtka                    | America/Atka                     | [Link](#tz_americaatka---_string_)                    |
| TZ_AmericaBahia                   | America/Bahia                    | [Link](#tz_americabahia---_string_)                   |
| TZ_AmericaBahiaBanderas           | America/Bahia_Banderas           | [Link](#tz_americabahiabanderas---_string_)           |
| TZ_AmericaBarbados                | America/Barbados                 | [Link](#tz_americabarbados---_string_)                |
| TZ_AmericaBelem                   | America/Belem                    | [Link](#tz_americabelem---_string_)                   |
| TZ_AmericaBelize                  | America/Belize                   | [Link](#tz_americabelize---_string_)                  |
| TZ_AmericaBlanc-Sablon            | America/Blanc-Sablon             | [Link](#tz_americablanc-sablon---_string_)            |
| TZ_AmericaBoaVista                | America/Boa_Vista                | [Link](#tz_americaboavista---_string_)                |
| TZ_AmericaBogota                  | America/Bogota                   | [Link](#tz_americabogota---_string_)                  |
| TZ_AmericaBoise                   | America/Boise                    | [Link](#tz_americaboise---_string_)                   |
| TZ_AmericaBuenosAires             | America/Buenos_Aires             | [Link](#tz_americabuenosaires---_string_)             |
| TZ_AmericaCambridgeBay            | America/Cambridge_Bay            | [Link](#tz_americacambridgebay---_string_)            |
| TZ_AmericaCampoGrande             | America/Campo_Grande             | [Link](#tz_americacampogrande---_string_)             |
| TZ_AmericaCancun                  | America/Cancun                   | [Link](#tz_americacancun---_string_)                  |
| TZ_AmericaCaracas                 | America/Caracas                  | [Link](#tz_americacaracas---_string_)                 |
| TZ_AmericaCatamarca               | America/Catamarca                | [Link](#tz_americacatamarca---_string_)               |
| TZ_AmericaCayenne                 | America/Cayenne                  | [Link](#tz_americacayenne---_string_)                 |
| TZ_AmericaCayman                  | America/Cayman                   | [Link](#tz_americacayman---_string_)                  |
| TZ_AmericaChicago                 | America/Chicago                  | [Link](#tz_americachicago---_string_)                 |
| TZ_AmericaChihuahua               | America/Chihuahua                | [Link](#tz_americachihuahua---_string_)               |
| TZ_AmericaCiudadJuarez            | America/Ciudad_Juarez            | [Link](#tz_americaciudadjuarez---_string_)            |
| TZ_AmericaCoralHarbour            | America/Coral_Harbour            | [Link](#tz_americacoralharbour---_string_)            |
| TZ_AmericaCordoba                 | America/Cordoba                  | [Link](#tz_americacordoba---_string_)                 |
| TZ_AmericaCostaRica               | America/Costa_Rica               | [Link](#tz_americacostarica---_string_)               |
| TZ_AmericaCreston                 | America/Creston                  | [Link](#tz_americacreston---_string_)                 |
| TZ_AmericaCuiaba                  | America/Cuiaba                   | [Link](#tz_americacuiaba---_string_)                  |
| TZ_AmericaCuracao                 | America/Curacao                  | [Link](#tz_americacuracao---_string_)                 |
| TZ_AmericaDanmarkshavn            | America/Danmarkshavn             | [Link](#tz_americadanmarkshavn---_string_)            |
| TZ_AmericaDawson                  | America/Dawson                   | [Link](#tz_americadawson---_string_)                  |
| TZ_AmericaDawsonCreek             | America/Dawson_Creek             | [Link](#tz_americadawsoncreek---_string_)             |
| TZ_AmericaDenver                  | America/Denver                   | [Link](#tz_americadenver---_string_)                  |
| TZ_AmericaDetroit                 | America/Detroit                  | [Link](#tz_americadetroit---_string_)                 |
| TZ_AmericaDominica                | America/Dominica                 | [Link](#tz_americadominica---_string_)                |
| TZ_AmericaEdmonton                | America/Edmonton                 | [Link](#tz_americaedmonton---_string_)                |
| TZ_AmericaEirunepe                | America/Eirunepe                 | [Link](#tz_americaeirunepe---_string_)                |
| TZ_AmericaElSalvador              | America/El_Salvador              | [Link](#tz_americaelsalvador---_string_)              |
| TZ_AmericaEnsenada                | America/Ensenada                 | [Link](#tz_americaensenada---_string_)                |
| TZ_AmericaFortNelson              | America/Fort_Nelson              | [Link](#tz_americafortnelson---_string_)              |
| TZ_AmericaFortWayne               | America/Fort_Wayne               | [Link](#tz_americafortwayne---_string_)               |
| TZ_AmericaFortaleza               | America/Fortaleza                | [Link](#tz_americafortaleza---_string_)               |
| TZ_AmericaGlaceBay                | America/Glace_Bay                | [Link](#tz_americaglacebay---_string_)                |
| TZ_AmericaGodthab                 | America/Godthab                  | [Link](#tz_americagodthab---_string_)                 |
| TZ_AmericaGooseBay                | America/Goose_Bay                | [Link](#tz_americagoosebay---_string_)                |
| TZ_AmericaGrandTurk               | America/Grand_Turk               | [Link](#tz_americagrandturk---_string_)               |
| TZ_AmericaGrenada                 | America/Grenada                  | [Link](#tz_americagrenada---_string_)                 |
| TZ_AmericaGuadeloupe              | America/Guadeloupe               | [Link](#tz_americaguadeloupe---_string_)              |
| TZ_AmericaGuatemala               | America/Guatemala                | [Link](#tz_americaguatemala---_string_)               |
| TZ_AmericaGuayaquil               | America/Guayaquil                | [Link](#tz_americaguayaquil---_string_)               |
| TZ_AmericaGuyana                  | America/Guyana                   | [Link](#tz_americaguyana---_string_)                  |
| TZ_AmericaHalifax                 | America/Halifax                  | [Link](#tz_americahalifax---_string_)                 |
| TZ_AmericaHavana                  | America/Havana                   | [Link](#tz_americahavana---_string_)                  |
| TZ_AmericaHermosillo              | America/Hermosillo               | [Link](#tz_americahermosillo---_string_)              |
| TZ_AmericaIndianaIndianapolis     | America/Indiana/Indianapolis     | [Link](#tz_americaindianaindianapolis---_string_)     |
| TZ_AmericaIndianaKnox             | America/Indiana/Knox             | [Link](#tz_americaindianaknox---_string_)             |
| TZ_AmericaIndianaMarengo          | America/Indiana/Marengo          | [Link](#tz_americaindianamarengo---_string_)          |
| TZ_AmericaIndianaPetersburg       | America/Indiana/Petersburg       | [Link](#tz_americaindianapetersburg---_string_)       |
| TZ_AmericaIndianaTellCity         | America/Indiana/Tell_City        | [Link](#tz_americaindianatellcity---_string_)         |
| TZ_AmericaIndianaVevay            | America/Indiana/Vevay            | [Link](#tz_americaindianavevay---_string_)            |
| TZ_AmericaIndianaVincennes        | America/Indiana/Vincennes        | [Link](#tz_americaindianavincennes---_string_)        |
| TZ_AmericaIndianaWinamac          | America/Indiana/Winamac          | [Link](#tz_americaindianawinamac---_string_)          |
| TZ_AmericaIndianapolis            | America/Indianapolis             | [Link](#tz_americaindianapolis---_string_)            |
| TZ_AmericaInuvik                  | America/Inuvik                   | [Link](#tz_americainuvik---_string_)                  |
| TZ_AmericaIqaluit                 | America/Iqaluit                  | [Link](#tz_americaiqaluit---_string_)                 |
| TZ_AmericaJamaica                 | America/Jamaica                  | [Link](#tz_americajamaica---_string_)                 |
| TZ_AmericaJujuy                   | America/Jujuy                    | [Link](#tz_americajujuy---_string_)                   |
| TZ_AmericaJuneau                  | America/Juneau                   | [Link](#tz_americajuneau---_string_)                  |
| TZ_AmericaKentuckyLouisville      | America/Kentucky/Louisville      | [Link](#tz_americakentuckylouisville---_string_)      |
| TZ_AmericaKentuckyMonticello      | America/Kentucky/Monticello      | [Link](#tz_americakentuckymonticello---_string_)      |
| TZ_AmericaKnoxIN                  | America/Knox_IN                  | [Link](#tz_americaknoxin---_string_)                  |
| TZ_AmericaKralendijk              | America/Kralendijk               | [Link](#tz_americakralendijk---_string_)              |
| TZ_AmericaLaPaz                   | America/La_Paz                   | [Link](#tz_americalapaz---_string_)                   |
| TZ_AmericaLima                    | America/Lima                     | [Link](#tz_americalima---_string_)                    |
| TZ_AmericaLosAngeles              | America/Los_Angeles              | [Link](#tz_americalosangeles---_string_)              |
| TZ_AmericaLouisville              | America/Louisville               | [Link](#tz_americalouisville---_string_)              |
| TZ_AmericaLowerPrinces            | America/Lower_Princes            | [Link](#tz_americalowerprinces---_string_)            |
| TZ_AmericaMaceio                  | America/Maceio                   | [Link](#tz_americamaceio---_string_)                  |
| TZ_AmericaManagua                 | America/Managua                  | [Link](#tz_americamanagua---_string_)                 |
| TZ_AmericaManaus                  | America/Manaus                   | [Link](#tz_americamanaus---_string_)                  |
| TZ_AmericaMarigot                 | America/Marigot                  | [Link](#tz_americamarigot---_string_)                 |
| TZ_AmericaMartinique              | America/Martinique               | [Link](#tz_americamartinique---_string_)              |
| TZ_AmericaMatamoros               | America/Matamoros                | [Link](#tz_americamatamoros---_string_)               |
| TZ_AmericaMazatlan                | America/Mazatlan                 | [Link](#tz_americamazatlan---_string_)                |
| TZ_AmericaMendoza                 | America/Mendoza                  | [Link](#tz_americamendoza---_string_)                 |
| TZ_AmericaMenominee               | America/Menominee                | [Link](#tz_americamenominee---_string_)               |
| TZ_AmericaMerida                  | America/Merida                   | [Link](#tz_americamerida---_string_)                  |
| TZ_AmericaMetlakatla              | America/Metlakatla               | [Link](#tz_americametlakatla---_string_)              |
| TZ_AmericaMexicoCity              | America/Mexico_City              | [Link](#tz_americamexicocity---_string_)              |
| TZ_AmericaMiquelon                | America/Miquelon                 | [Link](#tz_americamiquelon---_string_)                |
| TZ_AmericaMoncton                 | America/Moncton                  | [Link](#tz_americamoncton---_string_)                 |
| TZ_AmericaMonterrey               | America/Monterrey                | [Link](#tz_americamonterrey---_string_)               |
| TZ_AmericaMontevideo              | America/Montevideo               | [Link](#tz_americamontevideo---_string_)              |
| TZ_AmericaMontreal                | America/Montreal                 | [Link](#tz_americamontreal---_string_)                |
| TZ_AmericaMontserrat              | America/Montserrat               | [Link](#tz_americamontserrat---_string_)              |
| TZ_AmericaNassau                  | America/Nassau                   | [Link](#tz_americanassau---_string_)                  |
| TZ_AmericaNewYork                 | America/New_York                 | [Link](#tz_americanewyork---_string_)                 |
| TZ_AmericaNipigon                 | America/Nipigon                  | [Link](#tz_americanipigon---_string_)                 |
| TZ_AmericaNome                    | America/Nome                     | [Link](#tz_americanome---_string_)                    |
| TZ_AmericaNoronha                 | America/Noronha                  | [Link](#tz_americanoronha---_string_)                 |
| TZ_AmericaNorthDakotaBeulah       | America/North_Dakota/Beulah      | [Link](#tz_americanorthdakotabeulah---_string_)       |
| TZ_AmericaNorthDakotaCenter       | America/North_Dakota/Center      | [Link](#tz_americanorthdakotacenter---_string_)       |
| TZ_AmericaNorthDakotaNewSalem     | America/North_Dakota/New_Salem   | [Link](#tz_americanorthdakotanewsalem---_string_)     |
| TZ_AmericaNuuk                    | America/Nuuk                     | [Link](#tz_americanuuk---_string_)                    |
| TZ_AmericaOjinaga                 | America/Ojinaga                  | [Link](#tz_americaojinaga---_string_)                 |
| TZ_AmericaPanama                  | America/Panama                   | [Link](#tz_americapanama---_string_)                  |
| TZ_AmericaPangnirtung             | America/Pangnirtung              | [Link](#tz_americapangnirtung---_string_)             |
| TZ_AmericaParamaribo              | America/Paramaribo               | [Link](#tz_americaparamaribo---_string_)              |
| TZ_AmericaPhoenix                 | America/Phoenix                  | [Link](#tz_americaphoenix---_string_)                 |
| TZ_AmericaPort-au-Prince          | America/Port-au-Prince           | [Link](#tz_americaport-au-prince---_string_)          |
| TZ_AmericaPortoAcre               | America/Porto_Acre               | [Link](#tz_americaportoacre---_string_)               |
| TZ_AmericaPortoVelho              | America/Porto_Velho              | [Link](#tz_americaportovelho---_string_)              |
| TZ_AmericaPortofSpain             | America/Port_of_Spain            | [Link](#tz_americaportofspain---_string_)             |
| TZ_AmericaPuertoRico              | America/Puerto_Rico              | [Link](#tz_americapuertorico---_string_)              |
| TZ_AmericaPuntaArenas             | America/Punta_Arenas             | [Link](#tz_americapuntaarenas---_string_)             |
| TZ_AmericaRainyRiver              | America/Rainy_River              | [Link](#tz_americarainyriver---_string_)              |
| TZ_AmericaRankinInlet             | America/Rankin_Inlet             | [Link](#tz_americarankininlet---_string_)             |
| TZ_AmericaRecife                  | America/Recife                   | [Link](#tz_americarecife---_string_)                  |
| TZ_AmericaRegina                  | America/Regina                   | [Link](#tz_americaregina---_string_)                  |
| TZ_AmericaResolute                | America/Resolute                 | [Link](#tz_americaresolute---_string_)                |
| TZ_AmericaRioBranco               | America/Rio_Branco               | [Link](#tz_americariobranco---_string_)               |
| TZ_AmericaRosario                 | America/Rosario                  | [Link](#tz_americarosario---_string_)                 |
| TZ_AmericaSantaIsabel             | America/Santa_Isabel             | [Link](#tz_americasantaisabel---_string_)             |
| TZ_AmericaSantarem                | America/Santarem                 | [Link](#tz_americasantarem---_string_)                |
| TZ_AmericaSantiago                | America/Santiago                 | [Link](#tz_americasantiago---_string_)                |
| TZ_AmericaSantoDomingo            | America/Santo_Domingo            | [Link](#tz_americasantodomingo---_string_)            |
| TZ_AmericaSaoPaulo                | America/Sao_Paulo                | [Link](#tz_americasaopaulo---_string_)                |
| TZ_AmericaScoresbysund            | America/Scoresbysund             | [Link](#tz_americascoresbysund---_string_)            |
| TZ_AmericaShiprock                | America/Shiprock                 | [Link](#tz_americashiprock---_string_)                |
| TZ_AmericaSitka                   | America/Sitka                    | [Link](#tz_americasitka---_string_)                   |
| TZ_AmericaStBarthelemy            | America/St_Barthelemy            | [Link](#tz_americastbarthelemy---_string_)            |
| TZ_AmericaStJohns                 | America/St_Johns                 | [Link](#tz_americastjohns---_string_)                 |
| TZ_AmericaStKitts                 | America/St_Kitts                 | [Link](#tz_americastkitts---_string_)                 |
| TZ_AmericaStLucia                 | America/St_Lucia                 | [Link](#tz_americastlucia---_string_)                 |
| TZ_AmericaStThomas                | America/St_Thomas                | [Link](#tz_americastthomas---_string_)                |
| TZ_AmericaStVincent               | America/St_Vincent               | [Link](#tz_americastvincent---_string_)               |
| TZ_AmericaSwiftCurrent            | America/Swift_Current            | [Link](#tz_americaswiftcurrent---_string_)            |
| TZ_AmericaTegucigalpa             | America/Tegucigalpa              | [Link](#tz_americategucigalpa---_string_)             |
| TZ_AmericaThule                   | America/Thule                    | [Link](#tz_americathule---_string_)                   |
| TZ_AmericaThunderBay              | America/Thunder_Bay              | [Link](#tz_americathunderbay---_string_)              |
| TZ_AmericaTijuana                 | America/Tijuana                  | [Link](#tz_americatijuana---_string_)                 |
| TZ_AmericaToronto                 | America/Toronto                  | [Link](#tz_americatoronto---_string_)                 |
| TZ_AmericaTortola                 | America/Tortola                  | [Link](#tz_americatortola---_string_)                 |
| TZ_AmericaVancouver               | America/Vancouver                | [Link](#tz_americavancouver---_string_)               |
| TZ_AmericaVirgin                  | America/Virgin                   | [Link](#tz_americavirgin---_string_)                  |
| TZ_AmericaWhitehorse              | America/Whitehorse               | [Link](#tz_americawhitehorse---_string_)              |
| TZ_AmericaWinnipeg                | America/Winnipeg                 | [Link](#tz_americawinnipeg---_string_)                |
| TZ_AmericaYakutat                 | America/Yakutat                  | [Link](#tz_americayakutat---_string_)                 |
| TZ_AmericaYellowknife             | America/Yellowknife              | [Link](#tz_americayellowknife---_string_)             |
| TZ_AntarcticaCasey                | Antarctica/Casey                 | [Link](#tz_antarcticacasey---_string_)                |
| TZ_AntarcticaDavis                | Antarctica/Davis                 | [Link](#tz_antarcticadavis---_string_)                |
| TZ_AntarcticaDumontDUrville       | Antarctica/DumontDUrville        | [Link](#tz_antarcticadumontdurville---_string_)       |
| TZ_AntarcticaMacquarie            | Antarctica/Macquarie             | [Link](#tz_antarcticamacquarie---_string_)            |
| TZ_AntarcticaMawson               | Antarctica/Mawson                | [Link](#tz_antarcticamawson---_string_)               |
| TZ_AntarcticaMcMurdo              | Antarctica/McMurdo               | [Link](#tz_antarcticamcmurdo---_string_)              |
| TZ_AntarcticaPalmer               | Antarctica/Palmer                | [Link](#tz_antarcticapalmer---_string_)               |
| TZ_AntarcticaRothera              | Antarctica/Rothera               | [Link](#tz_antarcticarothera---_string_)              |
| TZ_AntarcticaSouthPole            | Antarctica/South_Pole            | [Link](#tz_antarcticasouthpole---_string_)            |
| TZ_AntarcticaSyowa                | Antarctica/Syowa                 | [Link](#tz_antarcticasyowa---_string_)                |
| TZ_AntarcticaTroll                | Antarctica/Troll                 | [Link](#tz_antarcticatroll---_string_)                |
| TZ_AntarcticaVostok               | Antarctica/Vostok                | [Link](#tz_antarcticavostok---_string_)               |
| TZ_ArcticLongyearbyen             | Arctic/Longyearbyen              | [Link](#tz_arcticlongyearbyen---_string_)             |
| TZ_AsiaAden                       | Asia/Aden                        | [Link](#tz_asiaaden---_string_)                       |
| TZ_AsiaAlmaty                     | Asia/Almaty                      | [Link](#tz_asiaalmaty---_string_)                     |
| TZ_AsiaAmman                      | Asia/Amman                       | [Link](#tz_asiaamman---_string_)                      |
| TZ_AsiaAnadyr                     | Asia/Anadyr                      | [Link](#tz_asiaanadyr---_string_)                     |
| TZ_AsiaAqtau                      | Asia/Aqtau                       | [Link](#tz_asiaaqtau---_string_)                      |
| TZ_AsiaAqtobe                     | Asia/Aqtobe                      | [Link](#tz_asiaaqtobe---_string_)                     |
| TZ_AsiaAshgabat                   | Asia/Ashgabat                    | [Link](#tz_asiaashgabat---_string_)                   |
| TZ_AsiaAshkhabad                  | Asia/Ashkhabad                   | [Link](#tz_asiaashkhabad---_string_)                  |
| TZ_AsiaAtyrau                     | Asia/Atyrau                      | [Link](#tz_asiaatyrau---_string_)                     |
| TZ_AsiaBaghdad                    | Asia/Baghdad                     | [Link](#tz_asiabaghdad---_string_)                    |
| TZ_AsiaBahrain                    | Asia/Bahrain                     | [Link](#tz_asiabahrain---_string_)                    |
| TZ_AsiaBaku                       | Asia/Baku                        | [Link](#tz_asiabaku---_string_)                       |
| TZ_AsiaBangkok                    | Asia/Bangkok                     | [Link](#tz_asiabangkok---_string_)                    |
| TZ_AsiaBarnaul                    | Asia/Barnaul                     | [Link](#tz_asiabarnaul---_string_)                    |
| TZ_AsiaBeirut                     | Asia/Beirut                      | [Link](#tz_asiabeirut---_string_)                     |
| TZ_AsiaBishkek                    | Asia/Bishkek                     | [Link](#tz_asiabishkek---_string_)                    |
| TZ_AsiaBrunei                     | Asia/Brunei                      | [Link](#tz_asiabrunei---_string_)                     |
| TZ_AsiaCalcutta                   | Asia/Calcutta                    | [Link](#tz_asiacalcutta---_string_)                   |
| TZ_AsiaChita                      | Asia/Chita                       | [Link](#tz_asiachita---_string_)                      |
| TZ_AsiaChoibalsan                 | Asia/Choibalsan                  | [Link](#tz_asiachoibalsan---_string_)                 |
| TZ_AsiaChongqing                  | Asia/Chongqing                   | [Link](#tz_asiachongqing---_string_)                  |
| TZ_AsiaChungking                  | Asia/Chungking                   | [Link](#tz_asiachungking---_string_)                  |
| TZ_AsiaColombo                    | Asia/Colombo                     | [Link](#tz_asiacolombo---_string_)                    |
| TZ_AsiaDacca                      | Asia/Dacca                       | [Link](#tz_asiadacca---_string_)                      |
| TZ_AsiaDamascus                   | Asia/Damascus                    | [Link](#tz_asiadamascus---_string_)                   |
| TZ_AsiaDhaka                      | Asia/Dhaka                       | [Link](#tz_asiadhaka---_string_)                      |
| TZ_AsiaDili                       | Asia/Dili                        | [Link](#tz_asiadili---_string_)                       |
| TZ_AsiaDubai                      | Asia/Dubai                       | [Link](#tz_asiadubai---_string_)                      |
| TZ_AsiaDushanbe                   | Asia/Dushanbe                    | [Link](#tz_asiadushanbe---_string_)                   |
| TZ_AsiaFamagusta                  | Asia/Famagusta                   | [Link](#tz_asiafamagusta---_string_)                  |
| TZ_AsiaGaza                       | Asia/Gaza                        | [Link](#tz_asiagaza---_string_)                       |
| TZ_AsiaHarbin                     | Asia/Harbin                      | [Link](#tz_asiaharbin---_string_)                     |
| TZ_AsiaHebron                     | Asia/Hebron                      | [Link](#tz_asiahebron---_string_)                     |
| TZ_AsiaHoChiMinh                  | Asia/Ho_Chi_Minh                 | [Link](#tz_asiahochiminh---_string_)                  |
| TZ_AsiaHongKong                   | Asia/Hong_Kong                   | [Link](#tz_asiahongkong---_string_)                   |
| TZ_AsiaHovd                       | Asia/Hovd                        | [Link](#tz_asiahovd---_string_)                       |
| TZ_AsiaIrkutsk                    | Asia/Irkutsk                     | [Link](#tz_asiairkutsk---_string_)                    |
| TZ_AsiaIstanbul                   | Asia/Istanbul                    | [Link](#tz_asiaistanbul---_string_)                   |
| TZ_AsiaJakarta                    | Asia/Jakarta                     | [Link](#tz_asiajakarta---_string_)                    |
| TZ_AsiaJayapura                   | Asia/Jayapura                    | [Link](#tz_asiajayapura---_string_)                   |
| TZ_AsiaJerusalem                  | Asia/Jerusalem                   | [Link](#tz_asiajerusalem---_string_)                  |
| TZ_AsiaKabul                      | Asia/Kabul                       | [Link](#tz_asiakabul---_string_)                      |
| TZ_AsiaKamchatka                  | Asia/Kamchatka                   | [Link](#tz_asiakamchatka---_string_)                  |
| TZ_AsiaKarachi                    | Asia/Karachi                     | [Link](#tz_asiakarachi---_string_)                    |
| TZ_AsiaKashgar                    | Asia/Kashgar                     | [Link](#tz_asiakashgar---_string_)                    |
| TZ_AsiaKathmandu                  | Asia/Kathmandu                   | [Link](#tz_asiakathmandu---_string_)                  |
| TZ_AsiaKatmandu                   | Asia/Katmandu                    | [Link](#tz_asiakatmandu---_string_)                   |
| TZ_AsiaKhandyga                   | Asia/Khandyga                    | [Link](#tz_asiakhandyga---_string_)                   |
| TZ_AsiaKolkata                    | Asia/Kolkata                     | [Link](#tz_asiakolkata---_string_)                    |
| TZ_AsiaKrasnoyarsk                | Asia/Krasnoyarsk                 | [Link](#tz_asiakrasnoyarsk---_string_)                |
| TZ_AsiaKualaLumpur                | Asia/Kuala_Lumpur                | [Link](#tz_asiakualalumpur---_string_)                |
| TZ_AsiaKuching                    | Asia/Kuching                     | [Link](#tz_asiakuching---_string_)                    |
| TZ_AsiaKuwait                     | Asia/Kuwait                      | [Link](#tz_asiakuwait---_string_)                     |
| TZ_AsiaMacao                      | Asia/Macao                       | [Link](#tz_asiamacao---_string_)                      |
| TZ_AsiaMacau                      | Asia/Macau                       | [Link](#tz_asiamacau---_string_)                      |
| TZ_AsiaMagadan                    | Asia/Magadan                     | [Link](#tz_asiamagadan---_string_)                    |
| TZ_AsiaMakassar                   | Asia/Makassar                    | [Link](#tz_asiamakassar---_string_)                   |
| TZ_AsiaManila                     | Asia/Manila                      | [Link](#tz_asiamanila---_string_)                     |
| TZ_AsiaMuscat                     | Asia/Muscat                      | [Link](#tz_asiamuscat---_string_)                     |
| TZ_AsiaNicosia                    | Asia/Nicosia                     | [Link](#tz_asianicosia---_string_)                    |
| TZ_AsiaNovokuznetsk               | Asia/Novokuznetsk                | [Link](#tz_asianovokuznetsk---_string_)               |
| TZ_AsiaNovosibirsk                | Asia/Novosibirsk                 | [Link](#tz_asianovosibirsk---_string_)                |
| TZ_AsiaOmsk                       | Asia/Omsk                        | [Link](#tz_asiaomsk---_string_)                       |
| TZ_AsiaOral                       | Asia/Oral                        | [Link](#tz_asiaoral---_string_)                       |
| TZ_AsiaPhnomPenh                  | Asia/Phnom_Penh                  | [Link](#tz_asiaphnompenh---_string_)                  |
| TZ_AsiaPontianak                  | Asia/Pontianak                   | [Link](#tz_asiapontianak---_string_)                  |
| TZ_AsiaPyongyang                  | Asia/Pyongyang                   | [Link](#tz_asiapyongyang---_string_)                  |
| TZ_AsiaQatar                      | Asia/Qatar                       | [Link](#tz_asiaqatar---_string_)                      |
| TZ_AsiaQostanay                   | Asia/Qostanay                    | [Link](#tz_asiaqostanay---_string_)                   |
| TZ_AsiaQyzylorda                  | Asia/Qyzylorda                   | [Link](#tz_asiaqyzylorda---_string_)                  |
| TZ_AsiaRangoon                    | Asia/Rangoon                     | [Link](#tz_asiarangoon---_string_)                    |
| TZ_AsiaRiyadh                     | Asia/Riyadh                      | [Link](#tz_asiariyadh---_string_)                     |
| TZ_AsiaSaigon                     | Asia/Saigon                      | [Link](#tz_asiasaigon---_string_)                     |
| TZ_AsiaSakhalin                   | Asia/Sakhalin                    | [Link](#tz_asiasakhalin---_string_)                   |
| TZ_AsiaSamarkand                  | Asia/Samarkand                   | [Link](#tz_asiasamarkand---_string_)                  |
| TZ_AsiaSeoul                      | Asia/Seoul                       | [Link](#tz_asiaseoul---_string_)                      |
| TZ_AsiaShanghai                   | Asia/Shanghai                    | [Link](#tz_asiashanghai---_string_)                   |
| TZ_AsiaSingapore                  | Asia/Singapore                   | [Link](#tz_asiasingapore---_string_)                  |
| TZ_AsiaSrednekolymsk              | Asia/Srednekolymsk               | [Link](#tz_asiasrednekolymsk---_string_)              |
| TZ_AsiaTaipei                     | Asia/Taipei                      | [Link](#tz_asiataipei---_string_)                     |
| TZ_AsiaTashkent                   | Asia/Tashkent                    | [Link](#tz_asiatashkent---_string_)                   |
| TZ_AsiaTbilisi                    | Asia/Tbilisi                     | [Link](#tz_asiatbilisi---_string_)                    |
| TZ_AsiaTehran                     | Asia/Tehran                      | [Link](#tz_asiatehran---_string_)                     |
| TZ_AsiaTelAviv                    | Asia/Tel_Aviv                    | [Link](#tz_asiatelaviv---_string_)                    |
| TZ_AsiaThimbu                     | Asia/Thimbu                      | [Link](#tz_asiathimbu---_string_)                     |
| TZ_AsiaThimphu                    | Asia/Thimphu                     | [Link](#tz_asiathimphu---_string_)                    |
| TZ_AsiaTokyo                      | Asia/Tokyo                       | [Link](#tz_asiatokyo---_string_)                      |
| TZ_AsiaTomsk                      | Asia/Tomsk                       | [Link](#tz_asiatomsk---_string_)                      |
| TZ_AsiaUjungPandang               | Asia/Ujung_Pandang               | [Link](#tz_asiaujungpandang---_string_)               |
| TZ_AsiaUlaanbaatar                | Asia/Ulaanbaatar                 | [Link](#tz_asiaulaanbaatar---_string_)                |
| TZ_AsiaUlanBator                  | Asia/Ulan_Bator                  | [Link](#tz_asiaulanbator---_string_)                  |
| TZ_AsiaUrumqi                     | Asia/Urumqi                      | [Link](#tz_asiaurumqi---_string_)                     |
| TZ_AsiaUst-Nera                   | Asia/Ust-Nera                    | [Link](#tz_asiaust-nera---_string_)                   |
| TZ_AsiaVientiane                  | Asia/Vientiane                   | [Link](#tz_asiavientiane---_string_)                  |
| TZ_AsiaVladivostok                | Asia/Vladivostok                 | [Link](#tz_asiavladivostok---_string_)                |
| TZ_AsiaYakutsk                    | Asia/Yakutsk                     | [Link](#tz_asiayakutsk---_string_)                    |
| TZ_AsiaYangon                     | Asia/Yangon                      | [Link](#tz_asiayangon---_string_)                     |
| TZ_AsiaYekaterinburg              | Asia/Yekaterinburg               | [Link](#tz_asiayekaterinburg---_string_)              |
| TZ_AsiaYerevan                    | Asia/Yerevan                     | [Link](#tz_asiayerevan---_string_)                    |
| TZ_AtlanticAzores                 | Atlantic/Azores                  | [Link](#tz_atlanticazores---_string_)                 |
| TZ_AtlanticBermuda                | Atlantic/Bermuda                 | [Link](#tz_atlanticbermuda---_string_)                |
| TZ_AtlanticCanary                 | Atlantic/Canary                  | [Link](#tz_atlanticcanary---_string_)                 |
| TZ_AtlanticCapeVerde              | Atlantic/Cape_Verde              | [Link](#tz_atlanticcapeverde---_string_)              |
| TZ_AtlanticFaeroe                 | Atlantic/Faeroe                  | [Link](#tz_atlanticfaeroe---_string_)                 |
| TZ_AtlanticFaroe                  | Atlantic/Faroe                   | [Link](#tz_atlanticfaroe---_string_)                  |
| TZ_AtlanticJanMayen               | Atlantic/Jan_Mayen               | [Link](#tz_atlanticjanmayen---_string_)               |
| TZ_AtlanticMadeira                | Atlantic/Madeira                 | [Link](#tz_atlanticmadeira---_string_)                |
| TZ_AtlanticReykjavik              | Atlantic/Reykjavik               | [Link](#tz_atlanticreykjavik---_string_)              |
| TZ_AtlanticSouthGeorgia           | Atlantic/South_Georgia           | [Link](#tz_atlanticsouthgeorgia---_string_)           |
| TZ_AtlanticStHelena               | Atlantic/St_Helena               | [Link](#tz_atlanticsthelena---_string_)               |
| TZ_AtlanticStanley                | Atlantic/Stanley                 | [Link](#tz_atlanticstanley---_string_)                |
| TZ_AustraliaACT                   | Australia/ACT                    | [Link](#tz_australiaact---_string_)                   |
| TZ_AustraliaAdelaide              | Australia/Adelaide               | [Link](#tz_australiaadelaide---_string_)              |
| TZ_AustraliaBrisbane              | Australia/Brisbane               | [Link](#tz_australiabrisbane---_string_)              |
| TZ_AustraliaBrokenHill            | Australia/Broken_Hill            | [Link](#tz_australiabrokenhill---_string_)            |
| TZ_AustraliaCanberra              | Australia/Canberra               | [Link](#tz_australiacanberra---_string_)              |
| TZ_AustraliaCurrie                | Australia/Currie                 | [Link](#tz_australiacurrie---_string_)                |
| TZ_AustraliaDarwin                | Australia/Darwin                 | [Link](#tz_australiadarwin---_string_)                |
| TZ_AustraliaEucla                 | Australia/Eucla                  | [Link](#tz_australiaeucla---_string_)                 |
| TZ_AustraliaHobart                | Australia/Hobart                 | [Link](#tz_australiahobart---_string_)                |
| TZ_AustraliaLHI                   | Australia/LHI                    | [Link](#tz_australialhi---_string_)                   |
| TZ_AustraliaLindeman              | Australia/Lindeman               | [Link](#tz_australialindeman---_string_)              |
| TZ_AustraliaLordHowe              | Australia/Lord_Howe              | [Link](#tz_australialordhowe---_string_)              |
| TZ_AustraliaMelbourne             | Australia/Melbourne              | [Link](#tz_australiamelbourne---_string_)             |
| TZ_AustraliaNSW                   | Australia/NSW                    | [Link](#tz_australiansw---_string_)                   |
| TZ_AustraliaNorth                 | Australia/North                  | [Link](#tz_australianorth---_string_)                 |
| TZ_AustraliaPerth                 | Australia/Perth                  | [Link](#tz_australiaperth---_string_)                 |
| TZ_AustraliaQueensland            | Australia/Queensland             | [Link](#tz_australiaqueensland---_string_)            |
| TZ_AustraliaSouth                 | Australia/South                  | [Link](#tz_australiasouth---_string_)                 |
| TZ_AustraliaSydney                | Australia/Sydney                 | [Link](#tz_australiasydney---_string_)                |
| TZ_AustraliaTasmania              | Australia/Tasmania               | [Link](#tz_australiatasmania---_string_)              |
| TZ_AustraliaVictoria              | Australia/Victoria               | [Link](#tz_australiavictoria---_string_)              |
| TZ_AustraliaWest                  | Australia/West                   | [Link](#tz_australiawest---_string_)                  |
| TZ_AustraliaYancowinna            | Australia/Yancowinna             | [Link](#tz_australiayancowinna---_string_)            |
| TZ_BrazilAcre                     | Brazil/Acre                      | [Link](#tz_brazilacre---_string_)                     |
| TZ_BrazilDeNoronha                | Brazil/DeNoronha                 | [Link](#tz_brazildenoronha---_string_)                |
| TZ_BrazilEast                     | Brazil/East                      | [Link](#tz_brazileast---_string_)                     |
| TZ_BrazilWest                     | Brazil/West                      | [Link](#tz_brazilwest---_string_)                     |
| TZ_CET                            | CET                              | [Link](#tz_cet---_string_)                            |
| TZ_CST6CDT                        | CST6CDT                          | [Link](#tz_cst6cdt---_string_)                        |
| TZ_CanadaAtlantic                 | Canada/Atlantic                  | [Link](#tz_canadaatlantic---_string_)                 |
| TZ_CanadaCentral                  | Canada/Central                   | [Link](#tz_canadacentral---_string_)                  |
| TZ_CanadaEastern                  | Canada/Eastern                   | [Link](#tz_canadaeastern---_string_)                  |
| TZ_CanadaMountain                 | Canada/Mountain                  | [Link](#tz_canadamountain---_string_)                 |
| TZ_CanadaNewfoundland             | Canada/Newfoundland              | [Link](#tz_canadanewfoundland---_string_)             |
| TZ_CanadaPacific                  | Canada/Pacific                   | [Link](#tz_canadapacific---_string_)                  |
| TZ_CanadaSaskatchewan             | Canada/Saskatchewan              | [Link](#tz_canadasaskatchewan---_string_)             |
| TZ_CanadaYukon                    | Canada/Yukon                     | [Link](#tz_canadayukon---_string_)                    |
| TZ_ChileContinental               | Chile/Continental                | [Link](#tz_chilecontinental---_string_)               |
| TZ_ChileEasterIsland              | Chile/EasterIsland               | [Link](#tz_chileeasterisland---_string_)              |
| TZ_Cuba                           | Cuba                             | [Link](#tz_cuba---_string_)                           |
| TZ_EET                            | EET                              | [Link](#tz_eet---_string_)                            |
| TZ_EST5EDT                        | EST5EDT                          | [Link](#tz_est5edt---_string_)                        |
| TZ_Egypt                          | Egypt                            | [Link](#tz_egypt---_string_)                          |
| TZ_Eire                           | Eire                             | [Link](#tz_eire---_string_)                           |
| TZ_EuropeAmsterdam                | Europe/Amsterdam                 | [Link](#tz_europeamsterdam---_string_)                |
| TZ_EuropeAndorra                  | Europe/Andorra                   | [Link](#tz_europeandorra---_string_)                  |
| TZ_EuropeAstrakhan                | Europe/Astrakhan                 | [Link](#tz_europeastrakhan---_string_)                |
| TZ_EuropeAthens                   | Europe/Athens                    | [Link](#tz_europeathens---_string_)                   |
| TZ_EuropeBelfast                  | Europe/Belfast                   | [Link](#tz_europebelfast---_string_)                  |
| TZ_EuropeBelgrade                 | Europe/Belgrade                  | [Link](#tz_europebelgrade---_string_)                 |
| TZ_EuropeBerlin                   | Europe/Berlin                    | [Link](#tz_europeberlin---_string_)                   |
| TZ_EuropeBratislava               | Europe/Bratislava                | [Link](#tz_europebratislava---_string_)               |
| TZ_EuropeBrussels                 | Europe/Brussels                  | [Link](#tz_europebrussels---_string_)                 |
| TZ_EuropeBucharest                | Europe/Bucharest                 | [Link](#tz_europebucharest---_string_)                |
| TZ_EuropeBudapest                 | Europe/Budapest                  | [Link](#tz_europebudapest---_string_)                 |
| TZ_EuropeBusingen                 | Europe/Busingen                  | [Link](#tz_europebusingen---_string_)                 |
| TZ_EuropeChisinau                 | Europe/Chisinau                  | [Link](#tz_europechisinau---_string_)                 |
| TZ_EuropeCopenhagen               | Europe/Copenhagen                | [Link](#tz_europecopenhagen---_string_)               |
| TZ_EuropeDublin                   | Europe/Dublin                    | [Link](#tz_europedublin---_string_)                   |
| TZ_EuropeGibraltar                | Europe/Gibraltar                 | [Link](#tz_europegibraltar---_string_)                |
| TZ_EuropeGuernsey                 | Europe/Guernsey                  | [Link](#tz_europeguernsey---_string_)                 |
| TZ_EuropeHelsinki                 | Europe/Helsinki                  | [Link](#tz_europehelsinki---_string_)                 |
| TZ_EuropeIsleofMan                | Europe/Isle_of_Man               | [Link](#tz_europeisleofman---_string_)                |
| TZ_EuropeIstanbul                 | Europe/Istanbul                  | [Link](#tz_europeistanbul---_string_)                 |
| TZ_EuropeJersey                   | Europe/Jersey                    | [Link](#tz_europejersey---_string_)                   |
| TZ_EuropeKaliningrad              | Europe/Kaliningrad               | [Link](#tz_europekaliningrad---_string_)              |
| TZ_EuropeKiev                     | Europe/Kiev                      | [Link](#tz_europekiev---_string_)                     |
| TZ_EuropeKirov                    | Europe/Kirov                     | [Link](#tz_europekirov---_string_)                    |
| TZ_EuropeKyiv                     | Europe/Kyiv                      | [Link](#tz_europekyiv---_string_)                     |
| TZ_EuropeLisbon                   | Europe/Lisbon                    | [Link](#tz_europelisbon---_string_)                   |
| TZ_EuropeLjubljana                | Europe/Ljubljana                 | [Link](#tz_europeljubljana---_string_)                |
| TZ_EuropeLondon                   | Europe/London                    | [Link](#tz_europelondon---_string_)                   |
| TZ_EuropeLuxembourg               | Europe/Luxembourg                | [Link](#tz_europeluxembourg---_string_)               |
| TZ_EuropeMadrid                   | Europe/Madrid                    | [Link](#tz_europemadrid---_string_)                   |
| TZ_EuropeMalta                    | Europe/Malta                     | [Link](#tz_europemalta---_string_)                    |
| TZ_EuropeMariehamn                | Europe/Mariehamn                 | [Link](#tz_europemariehamn---_string_)                |
| TZ_EuropeMinsk                    | Europe/Minsk                     | [Link](#tz_europeminsk---_string_)                    |
| TZ_EuropeMonaco                   | Europe/Monaco                    | [Link](#tz_europemonaco---_string_)                   |
| TZ_EuropeMoscow                   | Europe/Moscow                    | [Link](#tz_europemoscow---_string_)                   |
| TZ_EuropeNicosia                  | Europe/Nicosia                   | [Link](#tz_europenicosia---_string_)                  |
| TZ_EuropeOslo                     | Europe/Oslo                      | [Link](#tz_europeoslo---_string_)                     |
| TZ_EuropeParis                    | Europe/Paris                     | [Link](#tz_europeparis---_string_)                    |
| TZ_EuropePodgorica                | Europe/Podgorica                 | [Link](#tz_europepodgorica---_string_)                |
| TZ_EuropePrague                   | Europe/Prague                    | [Link](#tz_europeprague---_string_)                   |
| TZ_EuropeRiga                     | Europe/Riga                      | [Link](#tz_europeriga---_string_)                     |
| TZ_EuropeRome                     | Europe/Rome                      | [Link](#tz_europerome---_string_)                     |
| TZ_EuropeSamara                   | Europe/Samara                    | [Link](#tz_europesamara---_string_)                   |
| TZ_EuropeSanMarino                | Europe/San_Marino                | [Link](#tz_europesanmarino---_string_)                |
| TZ_EuropeSarajevo                 | Europe/Sarajevo                  | [Link](#tz_europesarajevo---_string_)                 |
| TZ_EuropeSaratov                  | Europe/Saratov                   | [Link](#tz_europesaratov---_string_)                  |
| TZ_EuropeSimferopol               | Europe/Simferopol                | [Link](#tz_europesimferopol---_string_)               |
| TZ_EuropeSkopje                   | Europe/Skopje                    | [Link](#tz_europeskopje---_string_)                   |
| TZ_EuropeSofia                    | Europe/Sofia                     | [Link](#tz_europesofia---_string_)                    |
| TZ_EuropeStockholm                | Europe/Stockholm                 | [Link](#tz_europestockholm---_string_)                |
| TZ_EuropeTallinn                  | Europe/Tallinn                   | [Link](#tz_europetallinn---_string_)                  |
| TZ_EuropeTirane                   | Europe/Tirane                    | [Link](#tz_europetirane---_string_)                   |
| TZ_EuropeTiraspol                 | Europe/Tiraspol                  | [Link](#tz_europetiraspol---_string_)                 |
| TZ_EuropeUlyanovsk                | Europe/Ulyanovsk                 | [Link](#tz_europeulyanovsk---_string_)                |
| TZ_EuropeUzhgorod                 | Europe/Uzhgorod                  | [Link](#tz_europeuzhgorod---_string_)                 |
| TZ_EuropeVaduz                    | Europe/Vaduz                     | [Link](#tz_europevaduz---_string_)                    |
| TZ_EuropeVatican                  | Europe/Vatican                   | [Link](#tz_europevatican---_string_)                  |
| TZ_EuropeVienna                   | Europe/Vienna                    | [Link](#tz_europevienna---_string_)                   |
| TZ_EuropeVilnius                  | Europe/Vilnius                   | [Link](#tz_europevilnius---_string_)                  |
| TZ_EuropeVolgograd                | Europe/Volgograd                 | [Link](#tz_europevolgograd---_string_)                |
| TZ_EuropeWarsaw                   | Europe/Warsaw                    | [Link](#tz_europewarsaw---_string_)                   |
| TZ_EuropeZagreb                   | Europe/Zagreb                    | [Link](#tz_europezagreb---_string_)                   |
| TZ_EuropeZaporozhye               | Europe/Zaporozhye                | [Link](#tz_europezaporozhye---_string_)               |
| TZ_EuropeZurich                   | Europe/Zurich                    | [Link](#tz_europezurich---_string_)                   |
| TZ_GB                             | GB                               | [Link](#tz_gb---_string_)                             |
| TZ_GB-Eire                        | GB-Eire                          | [Link](#tz_gb-eire---_string_)                        |
| TZ_GMT                            | Etc/GMT                          | [Link](#tz_gmt---_string_)                            |
| TZ_GMT0                           | Etc/GMT0                         | [Link](#tz_gmt0---_string_)                           |
| TZ_GMTAfter0                      | Etc/GMT-0                        | [Link](#tz_gmtafter0---_string_)                      |
| TZ_GMTAfter1                      | Etc/GMT-1                        | [Link](#tz_gmtafter1---_string_)                      |
| TZ_GMTAfter10                     | Etc/GMT-10                       | [Link](#tz_gmtafter10---_string_)                     |
| TZ_GMTAfter11                     | Etc/GMT-11                       | [Link](#tz_gmtafter11---_string_)                     |
| TZ_GMTAfter12                     | Etc/GMT-12                       | [Link](#tz_gmtafter12---_string_)                     |
| TZ_GMTAfter13                     | Etc/GMT-13                       | [Link](#tz_gmtafter13---_string_)                     |
| TZ_GMTAfter14                     | Etc/GMT-14                       | [Link](#tz_gmtafter14---_string_)                     |
| TZ_GMTAfter2                      | Etc/GMT-2                        | [Link](#tz_gmtafter2---_string_)                      |
| TZ_GMTAfter3                      | Etc/GMT-3                        | [Link](#tz_gmtafter3---_string_)                      |
| TZ_GMTAfter4                      | Etc/GMT-4                        | [Link](#tz_gmtafter4---_string_)                      |
| TZ_GMTAfter5                      | Etc/GMT-5                        | [Link](#tz_gmtafter5---_string_)                      |
| TZ_GMTAfter6                      | Etc/GMT-6                        | [Link](#tz_gmtafter6---_string_)                      |
| TZ_GMTAfter7                      | Etc/GMT-7                        | [Link](#tz_gmtafter7---_string_)                      |
| TZ_GMTAfter8                      | Etc/GMT-8                        | [Link](#tz_gmtafter8---_string_)                      |
| TZ_GMTAfter9                      | Etc/GMT-9                        | [Link](#tz_gmtafter9---_string_)                      |
| TZ_GMTBehind0                     | Etc/GMT+0                        | [Link](#tz_gmtbehind0---_string_)                     |
| TZ_GMTBehind1                     | Etc/GMT+1                        | [Link](#tz_gmtbehind1---_string_)                     |
| TZ_GMTBehind10                    | Etc/GMT+10                       | [Link](#tz_gmtbehind10---_string_)                    |
| TZ_GMTBehind11                    | Etc/GMT+11                       | [Link](#tz_gmtbehind11---_string_)                    |
| TZ_GMTBehind12                    | Etc/GMT+12                       | [Link](#tz_gmtbehind12---_string_)                    |
| TZ_GMTBehind2                     | Etc/GMT+2                        | [Link](#tz_gmtbehind2---_string_)                     |
| TZ_GMTBehind3                     | Etc/GMT+3                        | [Link](#tz_gmtbehind3---_string_)                     |
| TZ_GMTBehind4                     | Etc/GMT+4                        | [Link](#tz_gmtbehind4---_string_)                     |
| TZ_GMTBehind5                     | Etc/GMT+5                        | [Link](#tz_gmtbehind5---_string_)                     |
| TZ_GMTBehind6                     | Etc/GMT+6                        | [Link](#tz_gmtbehind6---_string_)                     |
| TZ_GMTBehind7                     | Etc/GMT+7                        | [Link](#tz_gmtbehind7---_string_)                     |
| TZ_GMTBehind8                     | Etc/GMT+8                        | [Link](#tz_gmtbehind8---_string_)                     |
| TZ_GMTBehind9                     | Etc/GMT+9                        | [Link](#tz_gmtbehind9---_string_)                     |
| TZ_Greenwich                      | Etc/Greenwich                    | [Link](#tz_greenwich---_string_)                      |
| TZ_Hongkong                       | Hongkong                         | [Link](#tz_hongkong---_string_)                       |
| TZ_Iceland                        | Iceland                          | [Link](#tz_iceland---_string_)                        |
| TZ_IndianAntananarivo             | Indian/Antananarivo              | [Link](#tz_indianantananarivo---_string_)             |
| TZ_IndianChagos                   | Indian/Chagos                    | [Link](#tz_indianchagos---_string_)                   |
| TZ_IndianChristmas                | Indian/Christmas                 | [Link](#tz_indianchristmas---_string_)                |
| TZ_IndianCocos                    | Indian/Cocos                     | [Link](#tz_indiancocos---_string_)                    |
| TZ_IndianComoro                   | Indian/Comoro                    | [Link](#tz_indiancomoro---_string_)                   |
| TZ_IndianKerguelen                | Indian/Kerguelen                 | [Link](#tz_indiankerguelen---_string_)                |
| TZ_IndianMahe                     | Indian/Mahe                      | [Link](#tz_indianmahe---_string_)                     |
| TZ_IndianMaldives                 | Indian/Maldives                  | [Link](#tz_indianmaldives---_string_)                 |
| TZ_IndianMauritius                | Indian/Mauritius                 | [Link](#tz_indianmauritius---_string_)                |
| TZ_IndianMayotte                  | Indian/Mayotte                   | [Link](#tz_indianmayotte---_string_)                  |
| TZ_IndianReunion                  | Indian/Reunion                   | [Link](#tz_indianreunion---_string_)                  |
| TZ_Iran                           | Iran                             | [Link](#tz_iran---_string_)                           |
| TZ_Israel                         | Israel                           | [Link](#tz_israel---_string_)                         |
| TZ_Jamaica                        | Jamaica                          | [Link](#tz_jamaica---_string_)                        |
| TZ_Japan                          | Japan                            | [Link](#tz_japan---_string_)                          |
| TZ_Kwajalein                      | Kwajalein                        | [Link](#tz_kwajalein---_string_)                      |
| TZ_Libya                          | Libya                            | [Link](#tz_libya---_string_)                          |
| TZ_MET                            | MET                              | [Link](#tz_met---_string_)                            |
| TZ_MST7MDT                        | MST7MDT                          | [Link](#tz_mst7mdt---_string_)                        |
| TZ_MexicoBajaNorte                | Mexico/BajaNorte                 | [Link](#tz_mexicobajanorte---_string_)                |
| TZ_MexicoBajaSur                  | Mexico/BajaSur                   | [Link](#tz_mexicobajasur---_string_)                  |
| TZ_MexicoGeneral                  | Mexico/General                   | [Link](#tz_mexicogeneral---_string_)                  |
| TZ_NZ                             | NZ                               | [Link](#tz_nz---_string_)                             |
| TZ_NZ-CHAT                        | NZ-CHAT                          | [Link](#tz_nz-chat---_string_)                        |
| TZ_Navajo                         | Navajo                           | [Link](#tz_navajo---_string_)                         |
| TZ_PRC                            | PRC                              | [Link](#tz_prc---_string_)                            |
| TZ_PST8PDT                        | PST8PDT                          | [Link](#tz_pst8pdt---_string_)                        |
| TZ_PacificApia                    | Pacific/Apia                     | [Link](#tz_pacificapia---_string_)                    |
| TZ_PacificAuckland                | Pacific/Auckland                 | [Link](#tz_pacificauckland---_string_)                |
| TZ_PacificBougainville            | Pacific/Bougainville             | [Link](#tz_pacificbougainville---_string_)            |
| TZ_PacificChatham                 | Pacific/Chatham                  | [Link](#tz_pacificchatham---_string_)                 |
| TZ_PacificChuuk                   | Pacific/Chuuk                    | [Link](#tz_pacificchuuk---_string_)                   |
| TZ_PacificEaster                  | Pacific/Easter                   | [Link](#tz_pacificeaster---_string_)                  |
| TZ_PacificEfate                   | Pacific/Efate                    | [Link](#tz_pacificefate---_string_)                   |
| TZ_PacificEnderbury               | Pacific/Enderbury                | [Link](#tz_pacificenderbury---_string_)               |
| TZ_PacificFakaofo                 | Pacific/Fakaofo                  | [Link](#tz_pacificfakaofo---_string_)                 |
| TZ_PacificFiji                    | Pacific/Fiji                     | [Link](#tz_pacificfiji---_string_)                    |
| TZ_PacificFunafuti                | Pacific/Funafuti                 | [Link](#tz_pacificfunafuti---_string_)                |
| TZ_PacificGalapagos               | Pacific/Galapagos                | [Link](#tz_pacificgalapagos---_string_)               |
| TZ_PacificGambier                 | Pacific/Gambier                  | [Link](#tz_pacificgambier---_string_)                 |
| TZ_PacificGuadalcanal             | Pacific/Guadalcanal              | [Link](#tz_pacificguadalcanal---_string_)             |
| TZ_PacificGuam                    | Pacific/Guam                     | [Link](#tz_pacificguam---_string_)                    |
| TZ_PacificHonolulu                | Pacific/Honolulu                 | [Link](#tz_pacifichonolulu---_string_)                |
| TZ_PacificJohnston                | Pacific/Johnston                 | [Link](#tz_pacificjohnston---_string_)                |
| TZ_PacificKanton                  | Pacific/Kanton                   | [Link](#tz_pacifickanton---_string_)                  |
| TZ_PacificKiritimati              | Pacific/Kiritimati               | [Link](#tz_pacifickiritimati---_string_)              |
| TZ_PacificKosrae                  | Pacific/Kosrae                   | [Link](#tz_pacifickosrae---_string_)                  |
| TZ_PacificKwajalein               | Pacific/Kwajalein                | [Link](#tz_pacifickwajalein---_string_)               |
| TZ_PacificMajuro                  | Pacific/Majuro                   | [Link](#tz_pacificmajuro---_string_)                  |
| TZ_PacificMarquesas               | Pacific/Marquesas                | [Link](#tz_pacificmarquesas---_string_)               |
| TZ_PacificMidway                  | Pacific/Midway                   | [Link](#tz_pacificmidway---_string_)                  |
| TZ_PacificNauru                   | Pacific/Nauru                    | [Link](#tz_pacificnauru---_string_)                   |
| TZ_PacificNiue                    | Pacific/Niue                     | [Link](#tz_pacificniue---_string_)                    |
| TZ_PacificNorfolk                 | Pacific/Norfolk                  | [Link](#tz_pacificnorfolk---_string_)                 |
| TZ_PacificNoumea                  | Pacific/Noumea                   | [Link](#tz_pacificnoumea---_string_)                  |
| TZ_PacificPagoPago                | Pacific/Pago_Pago                | [Link](#tz_pacificpagopago---_string_)                |
| TZ_PacificPalau                   | Pacific/Palau                    | [Link](#tz_pacificpalau---_string_)                   |
| TZ_PacificPitcairn                | Pacific/Pitcairn                 | [Link](#tz_pacificpitcairn---_string_)                |
| TZ_PacificPohnpei                 | Pacific/Pohnpei                  | [Link](#tz_pacificpohnpei---_string_)                 |
| TZ_PacificPonape                  | Pacific/Ponape                   | [Link](#tz_pacificponape---_string_)                  |
| TZ_PacificPortMoresby             | Pacific/Port_Moresby             | [Link](#tz_pacificportmoresby---_string_)             |
| TZ_PacificRarotonga               | Pacific/Rarotonga                | [Link](#tz_pacificrarotonga---_string_)               |
| TZ_PacificSaipan                  | Pacific/Saipan                   | [Link](#tz_pacificsaipan---_string_)                  |
| TZ_PacificSamoa                   | Pacific/Samoa                    | [Link](#tz_pacificsamoa---_string_)                   |
| TZ_PacificTahiti                  | Pacific/Tahiti                   | [Link](#tz_pacifictahiti---_string_)                  |
| TZ_PacificTarawa                  | Pacific/Tarawa                   | [Link](#tz_pacifictarawa---_string_)                  |
| TZ_PacificTongatapu               | Pacific/Tongatapu                | [Link](#tz_pacifictongatapu---_string_)               |
| TZ_PacificTruk                    | Pacific/Truk                     | [Link](#tz_pacifictruk---_string_)                    |
| TZ_PacificWake                    | Pacific/Wake                     | [Link](#tz_pacificwake---_string_)                    |
| TZ_PacificWallis                  | Pacific/Wallis                   | [Link](#tz_pacificwallis---_string_)                  |
| TZ_PacificYap                     | Pacific/Yap                      | [Link](#tz_pacificyap---_string_)                     |
| TZ_Poland                         | Poland                           | [Link](#tz_poland---_string_)                         |
| TZ_Portugal                       | Portugal                         | [Link](#tz_portugal---_string_)                       |
| TZ_ROK                            | ROK                              | [Link](#tz_rok---_string_)                            |
| TZ_Singapore                      | Singapore                        | [Link](#tz_singapore---_string_)                      |
| TZ_SystemVAST4                    | SystemV/AST4                     | [Link](#tz_systemvast4---_string_)                    |
| TZ_SystemVAST4ADT                 | SystemV/AST4ADT                  | [Link](#tz_systemvast4adt---_string_)                 |
| TZ_SystemVCST6                    | SystemV/CST6                     | [Link](#tz_systemvcst6---_string_)                    |
| TZ_SystemVCST6CDT                 | SystemV/CST6CDT                  | [Link](#tz_systemvcst6cdt---_string_)                 |
| TZ_SystemVEST5                    | SystemV/EST5                     | [Link](#tz_systemvest5---_string_)                    |
| TZ_SystemVEST5EDT                 | SystemV/EST5EDT                  | [Link](#tz_systemvest5edt---_string_)                 |
| TZ_SystemVHST10                   | SystemV/HST10                    | [Link](#tz_systemvhst10---_string_)                   |
| TZ_SystemVMST7                    | SystemV/MST7                     | [Link](#tz_systemvmst7---_string_)                    |
| TZ_SystemVMST7MDT                 | SystemV/MST7MDT                  | [Link](#tz_systemvmst7mdt---_string_)                 |
| TZ_SystemVPST8                    | SystemV/PST8                     | [Link](#tz_systemvpst8---_string_)                    |
| TZ_SystemVPST8PDT                 | SystemV/PST8PDT                  | [Link](#tz_systemvpst8pdt---_string_)                 |
| TZ_SystemVYST9                    | SystemV/YST9                     | [Link](#tz_systemvyst9---_string_)                    |
| TZ_SystemVYST9YDT                 | SystemV/YST9YDT                  | [Link](#tz_systemvyst9ydt---_string_)                 |
| TZ_Turkey                         | Turkey                           | [Link](#tz_turkey---_string_)                         |
| TZ_UCT                            | UCT                              | [Link](#tz_uct---_string_)                            |
| TZ_USAlaska                       | US/Alaska                        | [Link](#tz_usalaska---_string_)                       |
| TZ_USAleutian                     | US/Aleutian                      | [Link](#tz_usaleutian---_string_)                     |
| TZ_USArizona                      | US/Arizona                       | [Link](#tz_usarizona---_string_)                      |
| TZ_USCentral                      | US/Central                       | [Link](#tz_uscentral---_string_)                      |
| TZ_USEast-Indiana                 | US/East-Indiana                  | [Link](#tz_useast-indiana---_string_)                 |
| TZ_USEastern                      | US/Eastern                       | [Link](#tz_useastern---_string_)                      |
| TZ_USHawaii                       | US/Hawaii                        | [Link](#tz_ushawaii---_string_)                       |
| TZ_USIndiana-Starke               | US/Indiana-Starke                | [Link](#tz_usindiana-starke---_string_)               |
| TZ_USMichigan                     | US/Michigan                      | [Link](#tz_usmichigan---_string_)                     |
| TZ_USMountain                     | US/Mountain                      | [Link](#tz_usmountain---_string_)                     |
| TZ_USPacific                      | US/Pacific                       | [Link](#tz_uspacific---_string_)                      |
| TZ_USSamoa                        | US/Samoa                         | [Link](#tz_ussamoa---_string_)                        |
| TZ_UTC                            | Etc/UTC                          | [Link](#tz_utc---_string_)                            |
| TZ_Universal                      | Etc/Universal                    | [Link](#tz_universal---_string_)                      |
| TZ_W-SU                           | W-SU                             | [Link](#tz_w-su---_string_)                           |
| TZ_WET                            | WET                              | [Link](#tz_wet---_string_)                            |
| TZ_Zulu                           | Etc/Zulu                         | [Link](#tz_zulu---_string_)                           |

### `TZ_PacificTruk <- `_**`string`**_

Pacific/Truk zone constant.

### `TZ_BrazilEast <- `_**`string`**_

Brazil/East zone constant.

### `TZ_ROK <- `_**`string`**_

ROK zone constant.

### `TZ_AsiaHarbin <- `_**`string`**_

Asia/Harbin zone constant.

### `TZ_AmericaJamaica <- `_**`string`**_

America/Jamaica zone constant.

### `TZ_AsiaMakassar <- `_**`string`**_

Asia/Makassar zone constant.

### `TZ_PacificApia <- `_**`string`**_

Pacific/Apia zone constant.

### `TZ_AmericaArgentinaBuenosAires <- `_**`string`**_

America/Argentina/Buenos_Aires zone constant.

### `TZ_AsiaAshgabat <- `_**`string`**_

Asia/Ashgabat zone constant.

### `TZ_AsiaYerevan <- `_**`string`**_

Asia/Yerevan zone constant.

### `TZ_Cuba <- `_**`string`**_

Cuba zone constant.

### `TZ_EuropeMonaco <- `_**`string`**_

Europe/Monaco zone constant.

### `TZ_AustraliaNSW <- `_**`string`**_

Australia/NSW zone constant.

### `TZ_PacificWake <- `_**`string`**_

Pacific/Wake zone constant.

### `TZ_AmericaNipigon <- `_**`string`**_

America/Nipigon zone constant.

### `TZ_AntarcticaRothera <- `_**`string`**_

Antarctica/Rothera zone constant.

### `TZ_PacificHonolulu <- `_**`string`**_

Pacific/Honolulu zone constant.

### `TZ_AfricaDouala <- `_**`string`**_

Africa/Douala zone constant.

### `TZ_AsiaDili <- `_**`string`**_

Asia/Dili zone constant.

### `TZ_EuropeDublin <- `_**`string`**_

Europe/Dublin zone constant.

### `TZ_CST6CDT <- `_**`string`**_

CST6CDT zone constant.

### `TZ_SystemVHST10 <- `_**`string`**_

SystemV/HST10 zone constant.

### `TZ_AmericaNoronha <- `_**`string`**_

America/Noronha zone constant.

### `TZ_AntarcticaPalmer <- `_**`string`**_

Antarctica/Palmer zone constant.

### `TZ_AsiaKhandyga <- `_**`string`**_

Asia/Khandyga zone constant.

### `TZ_PST8PDT <- `_**`string`**_

PST8PDT zone constant.

### `TZ_PacificKwajalein <- `_**`string`**_

Pacific/Kwajalein zone constant.

### `TZ_PacificKiritimati <- `_**`string`**_

Pacific/Kiritimati zone constant.

### `TZ_AsiaUlanBator <- `_**`string`**_

Asia/Ulan_Bator zone constant.

### `TZ_AsiaKuwait <- `_**`string`**_

Asia/Kuwait zone constant.

### `TZ_AmericaIndianaVevay <- `_**`string`**_

America/Indiana/Vevay zone constant.

### `TZ_MexicoBajaSur <- `_**`string`**_

Mexico/BajaSur zone constant.

### `TZ_PacificGuam <- `_**`string`**_

Pacific/Guam zone constant.

### `TZ_AustraliaQueensland <- `_**`string`**_

Australia/Queensland zone constant.

### `TZ_PacificNiue <- `_**`string`**_

Pacific/Niue zone constant.

### `TZ_UTC <- `_**`string`**_

Etc/UTC zone constant.

### `TZ_AsiaThimbu <- `_**`string`**_

Asia/Thimbu zone constant.

### `TZ_PacificSaipan <- `_**`string`**_

Pacific/Saipan zone constant.

### `TZ_AfricaPorto-Novo <- `_**`string`**_

Africa/Porto-Novo zone constant.

### `TZ_AsiaKuching <- `_**`string`**_

Asia/Kuching zone constant.

### `TZ_AsiaQatar <- `_**`string`**_

Asia/Qatar zone constant.

### `TZ_CanadaMountain <- `_**`string`**_

Canada/Mountain zone constant.

### `TZ_EuropeUzhgorod <- `_**`string`**_

Europe/Uzhgorod zone constant.

### `TZ_AsiaNovokuznetsk <- `_**`string`**_

Asia/Novokuznetsk zone constant.

### `TZ_PacificRarotonga <- `_**`string`**_

Pacific/Rarotonga zone constant.

### `TZ_AmericaStVincent <- `_**`string`**_

America/St_Vincent zone constant.

### `TZ_AfricaMonrovia <- `_**`string`**_

Africa/Monrovia zone constant.

### `TZ_AtlanticMadeira <- `_**`string`**_

Atlantic/Madeira zone constant.

### `TZ_AmericaMazatlan <- `_**`string`**_

America/Mazatlan zone constant.

### `TZ_AmericaNuuk <- `_**`string`**_

America/Nuuk zone constant.

### `TZ_AustraliaSouth <- `_**`string`**_

Australia/South zone constant.

### `TZ_AmericaSitka <- `_**`string`**_

America/Sitka zone constant.

### `TZ_EuropePodgorica <- `_**`string`**_

Europe/Podgorica zone constant.

### `TZ_PacificFiji <- `_**`string`**_

Pacific/Fiji zone constant.

### `TZ_AmericaPortoVelho <- `_**`string`**_

America/Porto_Velho zone constant.

### `TZ_AsiaDushanbe <- `_**`string`**_

Asia/Dushanbe zone constant.

### `TZ_EuropeLjubljana <- `_**`string`**_

Europe/Ljubljana zone constant.

### `TZ_AmericaAruba <- `_**`string`**_

America/Aruba zone constant.

### `TZ_AntarcticaVostok <- `_**`string`**_

Antarctica/Vostok zone constant.

### `TZ_AmericaDenver <- `_**`string`**_

America/Denver zone constant.

### `TZ_PacificTarawa <- `_**`string`**_

Pacific/Tarawa zone constant.

### `TZ_AsiaSeoul <- `_**`string`**_

Asia/Seoul zone constant.

### `TZ_AmericaIndianaPetersburg <- `_**`string`**_

America/Indiana/Petersburg zone constant.

### `TZ_Libya <- `_**`string`**_

Libya zone constant.

### `TZ_AsiaUrumqi <- `_**`string`**_

Asia/Urumqi zone constant.

### `TZ_AsiaDhaka <- `_**`string`**_

Asia/Dhaka zone constant.

### `TZ_AustraliaVictoria <- `_**`string`**_

Australia/Victoria zone constant.

### `TZ_PacificJohnston <- `_**`string`**_

Pacific/Johnston zone constant.

### `TZ_USEast-Indiana <- `_**`string`**_

US/East-Indiana zone constant.

### `TZ_AtlanticAzores <- `_**`string`**_

Atlantic/Azores zone constant.

### `TZ_AmericaDawsonCreek <- `_**`string`**_

America/Dawson_Creek zone constant.

### `TZ_AsiaOral <- `_**`string`**_

Asia/Oral zone constant.

### `TZ_EuropeIsleofMan <- `_**`string`**_

Europe/Isle_of_Man zone constant.

### `TZ_AsiaDacca <- `_**`string`**_

Asia/Dacca zone constant.

### `TZ_AmericaMontreal <- `_**`string`**_

America/Montreal zone constant.

### `TZ_AsiaAnadyr <- `_**`string`**_

Asia/Anadyr zone constant.

### `TZ_EuropeSimferopol <- `_**`string`**_

Europe/Simferopol zone constant.

### `TZ_AmericaKnoxIN <- `_**`string`**_

America/Knox_IN zone constant.

### `TZ_AsiaPontianak <- `_**`string`**_

Asia/Pontianak zone constant.

### `TZ_AsiaUlaanbaatar <- `_**`string`**_

Asia/Ulaanbaatar zone constant.

### `TZ_EuropeKiev <- `_**`string`**_

Europe/Kiev zone constant.

### `TZ_AmericaIndianaMarengo <- `_**`string`**_

America/Indiana/Marengo zone constant.

### `TZ_AtlanticStanley <- `_**`string`**_

Atlantic/Stanley zone constant.

### `TZ_EuropeAthens <- `_**`string`**_

Europe/Athens zone constant.

### `TZ_AustraliaMelbourne <- `_**`string`**_

Australia/Melbourne zone constant.

### `TZ_PacificTongatapu <- `_**`string`**_

Pacific/Tongatapu zone constant.

### `TZ_AmericaAraguaina <- `_**`string`**_

America/Araguaina zone constant.

### `TZ_AmericaSwiftCurrent <- `_**`string`**_

America/Swift_Current zone constant.

### `TZ_PacificPagoPago <- `_**`string`**_

Pacific/Pago_Pago zone constant.

### `TZ_AfricaBrazzaville <- `_**`string`**_

Africa/Brazzaville zone constant.

### `TZ_CanadaSaskatchewan <- `_**`string`**_

Canada/Saskatchewan zone constant.

### `TZ_AsiaVladivostok <- `_**`string`**_

Asia/Vladivostok zone constant.

### `TZ_AtlanticBermuda <- `_**`string`**_

Atlantic/Bermuda zone constant.

### `TZ_AsiaSamarkand <- `_**`string`**_

Asia/Samarkand zone constant.

### `TZ_AustraliaLindeman <- `_**`string`**_

Australia/Lindeman zone constant.

### `TZ_AmericaScoresbysund <- `_**`string`**_

America/Scoresbysund zone constant.

### `TZ_AfricaDaresSalaam <- `_**`string`**_

Africa/Dar_es_Salaam zone constant.

### `TZ_PacificMajuro <- `_**`string`**_

Pacific/Majuro zone constant.

### `TZ_AsiaQostanay <- `_**`string`**_

Asia/Qostanay zone constant.

### `TZ_AtlanticFaroe <- `_**`string`**_

Atlantic/Faroe zone constant.

### `TZ_AustraliaBrokenHill <- `_**`string`**_

Australia/Broken_Hill zone constant.

### `TZ_AmericaFortNelson <- `_**`string`**_

America/Fort_Nelson zone constant.

### `TZ_AmericaIndianaWinamac <- `_**`string`**_

America/Indiana/Winamac zone constant.

### `TZ_AtlanticSouthGeorgia <- `_**`string`**_

Atlantic/South_Georgia zone constant.

### `TZ_AmericaPort-au-Prince <- `_**`string`**_

America/Port-au-Prince zone constant.

### `TZ_EuropeVienna <- `_**`string`**_

Europe/Vienna zone constant.

### `TZ_AsiaSingapore <- `_**`string`**_

Asia/Singapore zone constant.

### `TZ_EuropeSofia <- `_**`string`**_

Europe/Sofia zone constant.

### `TZ_USHawaii <- `_**`string`**_

US/Hawaii zone constant.

### `TZ_AmericaAsuncion <- `_**`string`**_

America/Asuncion zone constant.

### `TZ_AmericaToronto <- `_**`string`**_

America/Toronto zone constant.

### `TZ_AfricaBlantyre <- `_**`string`**_

Africa/Blantyre zone constant.

### `TZ_AmericaArgentinaSanLuis <- `_**`string`**_

America/Argentina/San_Luis zone constant.

### `TZ_AsiaAqtobe <- `_**`string`**_

Asia/Aqtobe zone constant.

### `TZ_AmericaGuatemala <- `_**`string`**_

America/Guatemala zone constant.

### `TZ_AustraliaLordHowe <- `_**`string`**_

Australia/Lord_Howe zone constant.

### `TZ_AmericaCampoGrande <- `_**`string`**_

America/Campo_Grande zone constant.

### `TZ_AsiaChongqing <- `_**`string`**_

Asia/Chongqing zone constant.

### `TZ_AsiaKamchatka <- `_**`string`**_

Asia/Kamchatka zone constant.

### `TZ_AmericaArgentinaLaRioja <- `_**`string`**_

America/Argentina/La_Rioja zone constant.

### `TZ_AmericaIndianaIndianapolis <- `_**`string`**_

America/Indiana/Indianapolis zone constant.

### `TZ_AmericaSantarem <- `_**`string`**_

America/Santarem zone constant.

### `TZ_AfricaOuagadougou <- `_**`string`**_

Africa/Ouagadougou zone constant.

### `TZ_IndianComoro <- `_**`string`**_

Indian/Comoro zone constant.

### `TZ_AfricaTimbuktu <- `_**`string`**_

Africa/Timbuktu zone constant.

### `TZ_AmericaNome <- `_**`string`**_

America/Nome zone constant.

### `TZ_AsiaSaigon <- `_**`string`**_

Asia/Saigon zone constant.

### `TZ_AsiaOmsk <- `_**`string`**_

Asia/Omsk zone constant.

### `TZ_EuropeMinsk <- `_**`string`**_

Europe/Minsk zone constant.

### `TZ_AfricaNdjamena <- `_**`string`**_

Africa/Ndjamena zone constant.

### `TZ_AmericaStLucia <- `_**`string`**_

America/St_Lucia zone constant.

### `TZ_AmericaInuvik <- `_**`string`**_

America/Inuvik zone constant.

### `TZ_AmericaMendoza <- `_**`string`**_

America/Mendoza zone constant.

### `TZ_AmericaArgentinaTucuman <- `_**`string`**_

America/Argentina/Tucuman zone constant.

### `TZ_EuropeVilnius <- `_**`string`**_

Europe/Vilnius zone constant.

### `TZ_AsiaTehran <- `_**`string`**_

Asia/Tehran zone constant.

### `TZ_AmericaIndianaVincennes <- `_**`string`**_

America/Indiana/Vincennes zone constant.

### `TZ_AfricaDakar <- `_**`string`**_

Africa/Dakar zone constant.

### `TZ_AmericaArgentinaJujuy <- `_**`string`**_

America/Argentina/Jujuy zone constant.

### `TZ_AfricaLuanda <- `_**`string`**_

Africa/Luanda zone constant.

### `TZ_BrazilDeNoronha <- `_**`string`**_

Brazil/DeNoronha zone constant.

### `TZ_EuropeOslo <- `_**`string`**_

Europe/Oslo zone constant.

### `TZ_MET <- `_**`string`**_

MET zone constant.

### `TZ_AsiaYangon <- `_**`string`**_

Asia/Yangon zone constant.

### `TZ_EuropeAmsterdam <- `_**`string`**_

Europe/Amsterdam zone constant.

### `TZ_AmericaHalifax <- `_**`string`**_

America/Halifax zone constant.

### `TZ_AmericaJuneau <- `_**`string`**_

America/Juneau zone constant.

### `TZ_AmericaAtikokan <- `_**`string`**_

America/Atikokan zone constant.

### `TZ_AustraliaLHI <- `_**`string`**_

Australia/LHI zone constant.

### `TZ_AsiaKashgar <- `_**`string`**_

Asia/Kashgar zone constant.

### `TZ_AsiaSrednekolymsk <- `_**`string`**_

Asia/Srednekolymsk zone constant.

### `TZ_AmericaDominica <- `_**`string`**_

America/Dominica zone constant.

### `TZ_EuropeBerlin <- `_**`string`**_

Europe/Berlin zone constant.

### `TZ_AsiaAqtau <- `_**`string`**_

Asia/Aqtau zone constant.

### `TZ_AsiaPyongyang <- `_**`string`**_

Asia/Pyongyang zone constant.

### `TZ_AmericaYellowknife <- `_**`string`**_

America/Yellowknife zone constant.

### `TZ_PacificKanton <- `_**`string`**_

Pacific/Kanton zone constant.

### `TZ_Greenwich <- `_**`string`**_

Etc/Greenwich zone constant.

### `TZ_PacificGalapagos <- `_**`string`**_

Pacific/Galapagos zone constant.

### `TZ_AmericaArgentinaUshuaia <- `_**`string`**_

America/Argentina/Ushuaia zone constant.

### `TZ_EuropeChisinau <- `_**`string`**_

Europe/Chisinau zone constant.

### `TZ_AsiaBarnaul <- `_**`string`**_

Asia/Barnaul zone constant.

### `TZ_AsiaJerusalem <- `_**`string`**_

Asia/Jerusalem zone constant.

### `TZ_NZ-CHAT <- `_**`string`**_

NZ-CHAT zone constant.

### `TZ_AmericaYakutat <- `_**`string`**_

America/Yakutat zone constant.

### `TZ_AfricaAsmara <- `_**`string`**_

Africa/Asmara zone constant.

### `TZ_AmericaResolute <- `_**`string`**_

America/Resolute zone constant.

### `TZ_AmericaSaoPaulo <- `_**`string`**_

America/Sao_Paulo zone constant.

### `TZ_EuropeBudapest <- `_**`string`**_

Europe/Budapest zone constant.

### `TZ_AsiaHovd <- `_**`string`**_

Asia/Hovd zone constant.

### `TZ_EuropeKyiv <- `_**`string`**_

Europe/Kyiv zone constant.

### `TZ_AmericaCordoba <- `_**`string`**_

America/Cordoba zone constant.

### `TZ_AntarcticaMawson <- `_**`string`**_

Antarctica/Mawson zone constant.

### `TZ_PacificYap <- `_**`string`**_

Pacific/Yap zone constant.

### `TZ_AmericaCuiaba <- `_**`string`**_

America/Cuiaba zone constant.

### `TZ_AfricaMaseru <- `_**`string`**_

Africa/Maseru zone constant.

### `TZ_USAleutian <- `_**`string`**_

US/Aleutian zone constant.

### `TZ_AustraliaSydney <- `_**`string`**_

Australia/Sydney zone constant.

### `TZ_AmericaPortoAcre <- `_**`string`**_

America/Porto_Acre zone constant.

### `TZ_AmericaNorthDakotaNewSalem <- `_**`string`**_

America/North_Dakota/New_Salem zone constant.

### `TZ_AmericaKentuckyMonticello <- `_**`string`**_

America/Kentucky/Monticello zone constant.

### `TZ_AsiaBishkek <- `_**`string`**_

Asia/Bishkek zone constant.

### `TZ_AsiaKrasnoyarsk <- `_**`string`**_

Asia/Krasnoyarsk zone constant.

### `TZ_AfricaMogadishu <- `_**`string`**_

Africa/Mogadishu zone constant.

### `TZ_EuropeMoscow <- `_**`string`**_

Europe/Moscow zone constant.

### `TZ_GB-Eire <- `_**`string`**_

GB-Eire zone constant.

### `TZ_AtlanticReykjavik <- `_**`string`**_

Atlantic/Reykjavik zone constant.

### `TZ_EuropeHelsinki <- `_**`string`**_

Europe/Helsinki zone constant.

### `TZ_PRC <- `_**`string`**_

PRC zone constant.

### `TZ_PacificBougainville <- `_**`string`**_

Pacific/Bougainville zone constant.

### `TZ_GMTBehind5 <- `_**`string`**_

Etc/GMT+5 zone constant.

### `TZ_GMTBehind6 <- `_**`string`**_

Etc/GMT+6 zone constant.

### `TZ_GMTBehind3 <- `_**`string`**_

Etc/GMT+3 zone constant.

### `TZ_GMTBehind4 <- `_**`string`**_

Etc/GMT+4 zone constant.

### `TZ_GMTBehind1 <- `_**`string`**_

Etc/GMT+1 zone constant.

### `TZ_GMTBehind2 <- `_**`string`**_

Etc/GMT+2 zone constant.

### `TZ_GMTBehind0 <- `_**`string`**_

Etc/GMT+0 zone constant.

### `TZ_PacificPohnpei <- `_**`string`**_

Pacific/Pohnpei zone constant.

### `TZ_AsiaHongKong <- `_**`string`**_

Asia/Hong_Kong zone constant.

### `TZ_GMTBehind9 <- `_**`string`**_

Etc/GMT+9 zone constant.

### `TZ_GMTBehind7 <- `_**`string`**_

Etc/GMT+7 zone constant.

### `TZ_GMTBehind8 <- `_**`string`**_

Etc/GMT+8 zone constant.

### `TZ_CanadaEastern <- `_**`string`**_

Canada/Eastern zone constant.

### `TZ_AfricaKigali <- `_**`string`**_

Africa/Kigali zone constant.

### `TZ_AmericaFortWayne <- `_**`string`**_

America/Fort_Wayne zone constant.

### `TZ_AntarcticaDavis <- `_**`string`**_

Antarctica/Davis zone constant.

### `TZ_AustraliaWest <- `_**`string`**_

Australia/West zone constant.

### `TZ_EuropeBelfast <- `_**`string`**_

Europe/Belfast zone constant.

### `TZ_AsiaMacau <- `_**`string`**_

Asia/Macau zone constant.

### `TZ_AsiaBrunei <- `_**`string`**_

Asia/Brunei zone constant.

### `TZ_AmericaCoralHarbour <- `_**`string`**_

America/Coral_Harbour zone constant.

### `TZ_AmericaIqaluit <- `_**`string`**_

America/Iqaluit zone constant.

### `TZ_AmericaCayman <- `_**`string`**_

America/Cayman zone constant.

### `TZ_AsiaAmman <- `_**`string`**_

Asia/Amman zone constant.

### `TZ_AustraliaHobart <- `_**`string`**_

Australia/Hobart zone constant.

### `TZ_AsiaTokyo <- `_**`string`**_

Asia/Tokyo zone constant.

### `TZ_Kwajalein <- `_**`string`**_

Kwajalein zone constant.

### `TZ_AmericaChicago <- `_**`string`**_

America/Chicago zone constant.

### `TZ_AmericaHermosillo <- `_**`string`**_

America/Hermosillo zone constant.

### `TZ_AmericaLouisville <- `_**`string`**_

America/Louisville zone constant.

### `TZ_AfricaKinshasa <- `_**`string`**_

Africa/Kinshasa zone constant.

### `TZ_AmericaIndianapolis <- `_**`string`**_

America/Indianapolis zone constant.

### `TZ_AsiaHebron <- `_**`string`**_

Asia/Hebron zone constant.

### `TZ_AmericaEnsenada <- `_**`string`**_

America/Ensenada zone constant.

### `TZ_AustraliaEucla <- `_**`string`**_

Australia/Eucla zone constant.

### `TZ_PacificFakaofo <- `_**`string`**_

Pacific/Fakaofo zone constant.

### `TZ_AmericaMoncton <- `_**`string`**_

America/Moncton zone constant.

### `TZ_PacificPortMoresby <- `_**`string`**_

Pacific/Port_Moresby zone constant.

### `TZ_PacificNoumea <- `_**`string`**_

Pacific/Noumea zone constant.

### `TZ_AsiaAtyrau <- `_**`string`**_

Asia/Atyrau zone constant.

### `TZ_AsiaTelAviv <- `_**`string`**_

Asia/Tel_Aviv zone constant.

### `TZ_EuropeSanMarino <- `_**`string`**_

Europe/San_Marino zone constant.

### `TZ_AmericaSantaIsabel <- `_**`string`**_

America/Santa_Isabel zone constant.

### `TZ_AsiaYakutsk <- `_**`string`**_

Asia/Yakutsk zone constant.

### `TZ_AtlanticCanary <- `_**`string`**_

Atlantic/Canary zone constant.

### `TZ_AustraliaNorth <- `_**`string`**_

Australia/North zone constant.

### `TZ_AmericaTijuana <- `_**`string`**_

America/Tijuana zone constant.

### `TZ_AfricaJohannesburg <- `_**`string`**_

Africa/Johannesburg zone constant.

### `TZ_AsiaKolkata <- `_**`string`**_

Asia/Kolkata zone constant.

### `TZ_AsiaChita <- `_**`string`**_

Asia/Chita zone constant.

### `TZ_AfricaConakry <- `_**`string`**_

Africa/Conakry zone constant.

### `TZ_AsiaTashkent <- `_**`string`**_

Asia/Tashkent zone constant.

### `TZ_AfricaKhartoum <- `_**`string`**_

Africa/Khartoum zone constant.

### `TZ_AntarcticaCasey <- `_**`string`**_

Antarctica/Casey zone constant.

### `TZ_EuropeLisbon <- `_**`string`**_

Europe/Lisbon zone constant.

### `TZ_PacificWallis <- `_**`string`**_

Pacific/Wallis zone constant.

### `TZ_USEastern <- `_**`string`**_

US/Eastern zone constant.

### `TZ_AmericaMerida <- `_**`string`**_

America/Merida zone constant.

### `TZ_AsiaNovosibirsk <- `_**`string`**_

Asia/Novosibirsk zone constant.

### `TZ_AfricaLagos <- `_**`string`**_

Africa/Lagos zone constant.

### `TZ_EuropeCopenhagen <- `_**`string`**_

Europe/Copenhagen zone constant.

### `TZ_AfricaLubumbashi <- `_**`string`**_

Africa/Lubumbashi zone constant.

### `TZ_AmericaGlaceBay <- `_**`string`**_

America/Glace_Bay zone constant.

### `TZ_AsiaBangkok <- `_**`string`**_

Asia/Bangkok zone constant.

### `TZ_EuropeKirov <- `_**`string`**_

Europe/Kirov zone constant.

### `TZ_AmericaIndianaTellCity <- `_**`string`**_

America/Indiana/Tell_City zone constant.

### `TZ_EuropeMadrid <- `_**`string`**_

Europe/Madrid zone constant.

### `TZ_AmericaBoise <- `_**`string`**_

America/Boise zone constant.

### `TZ_AmericaPuertoRico <- `_**`string`**_

America/Puerto_Rico zone constant.

### `TZ_AsiaKabul <- `_**`string`**_

Asia/Kabul zone constant.

### `TZ_GMT0 <- `_**`string`**_

Etc/GMT0 zone constant.

### `TZ_AsiaMacao <- `_**`string`**_

Asia/Macao zone constant.

### `TZ_AsiaGaza <- `_**`string`**_

Asia/Gaza zone constant.

### `TZ_AmericaCuracao <- `_**`string`**_

America/Curacao zone constant.

### `TZ_AmericaSantoDomingo <- `_**`string`**_

America/Santo_Domingo zone constant.

### `TZ_AmericaVirgin <- `_**`string`**_

America/Virgin zone constant.

### `TZ_EuropeNicosia <- `_**`string`**_

Europe/Nicosia zone constant.

### `TZ_Turkey <- `_**`string`**_

Turkey zone constant.

### `TZ_Egypt <- `_**`string`**_

Egypt zone constant.

### `TZ_PacificEnderbury <- `_**`string`**_

Pacific/Enderbury zone constant.

### `TZ_AfricaAlgiers <- `_**`string`**_

Africa/Algiers zone constant.

### `TZ_AmericaMiquelon <- `_**`string`**_

America/Miquelon zone constant.

### `TZ_AmericaPangnirtung <- `_**`string`**_

America/Pangnirtung zone constant.

### `TZ_AmericaSantiago <- `_**`string`**_

America/Santiago zone constant.

### `TZ_AfricaKampala <- `_**`string`**_

Africa/Kampala zone constant.

### `TZ_AmericaArgentinaCatamarca <- `_**`string`**_

America/Argentina/Catamarca zone constant.

### `TZ_AmericaKralendijk <- `_**`string`**_

America/Kralendijk zone constant.

### `TZ_AsiaJayapura <- `_**`string`**_

Asia/Jayapura zone constant.

### `TZ_AsiaYekaterinburg <- `_**`string`**_

Asia/Yekaterinburg zone constant.

### `TZ_AustraliaBrisbane <- `_**`string`**_

Australia/Brisbane zone constant.

### `TZ_AsiaKathmandu <- `_**`string`**_

Asia/Kathmandu zone constant.

### `TZ_AmericaEdmonton <- `_**`string`**_

America/Edmonton zone constant.

### `TZ_AmericaManaus <- `_**`string`**_

America/Manaus zone constant.

### `TZ_AfricaCairo <- `_**`string`**_

Africa/Cairo zone constant.

### `TZ_EST5EDT <- `_**`string`**_

EST5EDT zone constant.

### `TZ_AsiaManila <- `_**`string`**_

Asia/Manila zone constant.

### `TZ_Japan <- `_**`string`**_

Japan zone constant.

### `TZ_AmericaLowerPrinces <- `_**`string`**_

America/Lower_Princes zone constant.

### `TZ_EuropeZurich <- `_**`string`**_

Europe/Zurich zone constant.

### `TZ_AsiaShanghai <- `_**`string`**_

Asia/Shanghai zone constant.

### `TZ_EuropeGibraltar <- `_**`string`**_

Europe/Gibraltar zone constant.

### `TZ_MST7MDT <- `_**`string`**_

MST7MDT zone constant.

### `TZ_EuropeVatican <- `_**`string`**_

Europe/Vatican zone constant.

### `TZ_AsiaTaipei <- `_**`string`**_

Asia/Taipei zone constant.

### `TZ_AsiaBaku <- `_**`string`**_

Asia/Baku zone constant.

### `TZ_AsiaDubai <- `_**`string`**_

Asia/Dubai zone constant.

### `TZ_USMountain <- `_**`string`**_

US/Mountain zone constant.

### `TZ_AmericaArgentinaMendoza <- `_**`string`**_

America/Argentina/Mendoza zone constant.

### `TZ_AmericaCambridgeBay <- `_**`string`**_

America/Cambridge_Bay zone constant.

### `TZ_AtlanticJanMayen <- `_**`string`**_

Atlantic/Jan_Mayen zone constant.

### `TZ_GB <- `_**`string`**_

GB zone constant.

### `TZ_AfricaMaputo <- `_**`string`**_

Africa/Maputo zone constant.

### `TZ_EuropeIstanbul <- `_**`string`**_

Europe/Istanbul zone constant.

### `TZ_IndianKerguelen <- `_**`string`**_

Indian/Kerguelen zone constant.

### `TZ_EuropeMariehamn <- `_**`string`**_

Europe/Mariehamn zone constant.

### `TZ_AmericaRainyRiver <- `_**`string`**_

America/Rainy_River zone constant.

### `TZ_AfricaWindhoek <- `_**`string`**_

Africa/Windhoek zone constant.

### `TZ_SystemVCST6CDT <- `_**`string`**_

SystemV/CST6CDT zone constant.

### `TZ_EET <- `_**`string`**_

EET zone constant.

### `TZ_IndianMayotte <- `_**`string`**_

Indian/Mayotte zone constant.

### `TZ_SystemVYST9 <- `_**`string`**_

SystemV/YST9 zone constant.

### `TZ_AfricaAsmera <- `_**`string`**_

Africa/Asmera zone constant.

### `TZ_SystemVCST6 <- `_**`string`**_

SystemV/CST6 zone constant.

### `TZ_AfricaCeuta <- `_**`string`**_

Africa/Ceuta zone constant.

### `TZ_AmericaMartinique <- `_**`string`**_

America/Martinique zone constant.

### `TZ_IndianCocos <- `_**`string`**_

Indian/Cocos zone constant.

### `TZ_EuropeLondon <- `_**`string`**_

Europe/London zone constant.

### `TZ_EuropeJersey <- `_**`string`**_

Europe/Jersey zone constant.

### `TZ_AntarcticaMacquarie <- `_**`string`**_

Antarctica/Macquarie zone constant.

### `TZ_Hongkong <- `_**`string`**_

Hongkong zone constant.

### `TZ_EuropeAstrakhan <- `_**`string`**_

Europe/Astrakhan zone constant.

### `TZ_SystemVPST8PDT <- `_**`string`**_

SystemV/PST8PDT zone constant.

### `TZ_AsiaQyzylorda <- `_**`string`**_

Asia/Qyzylorda zone constant.

### `TZ_AtlanticStHelena <- `_**`string`**_

Atlantic/St_Helena zone constant.

### `TZ_AmericaCaracas <- `_**`string`**_

America/Caracas zone constant.

### `TZ_PacificEaster <- `_**`string`**_

Pacific/Easter zone constant.

### `TZ_AmericaArgentinaRioGallegos <- `_**`string`**_

America/Argentina/Rio_Gallegos zone constant.

### `TZ_AustraliaDarwin <- `_**`string`**_

Australia/Darwin zone constant.

### `TZ_EuropeStockholm <- `_**`string`**_

Europe/Stockholm zone constant.

### `TZ_AfricaLome <- `_**`string`**_

Africa/Lome zone constant.

### `TZ_UCT <- `_**`string`**_

UCT zone constant.

### `TZ_Jamaica <- `_**`string`**_

Jamaica zone constant.

### `TZ_CanadaAtlantic <- `_**`string`**_

Canada/Atlantic zone constant.

### `TZ_USAlaska <- `_**`string`**_

US/Alaska zone constant.

### `TZ_AmericaPortofSpain <- `_**`string`**_

America/Port_of_Spain zone constant.

### `TZ_AmericaGodthab <- `_**`string`**_

America/Godthab zone constant.

### `TZ_AmericaTegucigalpa <- `_**`string`**_

America/Tegucigalpa zone constant.

### `TZ_CanadaCentral <- `_**`string`**_

Canada/Central zone constant.

### `TZ_EuropeBrussels <- `_**`string`**_

Europe/Brussels zone constant.

### `TZ_EuropeBratislava <- `_**`string`**_

Europe/Bratislava zone constant.

### `TZ_PacificMidway <- `_**`string`**_

Pacific/Midway zone constant.

### `TZ_PacificTahiti <- `_**`string`**_

Pacific/Tahiti zone constant.

### `TZ_AmericaPuntaArenas <- `_**`string`**_

America/Punta_Arenas zone constant.

### `TZ_AsiaBaghdad <- `_**`string`**_

Asia/Baghdad zone constant.

### `TZ_ChileEasterIsland <- `_**`string`**_

Chile/EasterIsland zone constant.

### `TZ_SystemVEST5EDT <- `_**`string`**_

SystemV/EST5EDT zone constant.

### `TZ_AsiaRangoon <- `_**`string`**_

Asia/Rangoon zone constant.

### `TZ_USPacific <- `_**`string`**_

US/Pacific zone constant.

### `TZ_IndianChagos <- `_**`string`**_

Indian/Chagos zone constant.

### `TZ_AtlanticCapeVerde <- `_**`string`**_

Atlantic/Cape_Verde zone constant.

### `TZ_Portugal <- `_**`string`**_

Portugal zone constant.

### `TZ_AmericaGuyana <- `_**`string`**_

America/Guyana zone constant.

### `TZ_AmericaCancun <- `_**`string`**_

America/Cancun zone constant.

### `TZ_AmericaCostaRica <- `_**`string`**_

America/Costa_Rica zone constant.

### `TZ_AmericaRegina <- `_**`string`**_

America/Regina zone constant.

### `TZ_AsiaSakhalin <- `_**`string`**_

Asia/Sakhalin zone constant.

### `TZ_IndianAntananarivo <- `_**`string`**_

Indian/Antananarivo zone constant.

### `TZ_PacificGambier <- `_**`string`**_

Pacific/Gambier zone constant.

### `TZ_Eire <- `_**`string`**_

Eire zone constant.

### `TZ_AmericaNorthDakotaCenter <- `_**`string`**_

America/North_Dakota/Center zone constant.

### `TZ_GMTAfter12 <- `_**`string`**_

Etc/GMT-12 zone constant.

### `TZ_GMTAfter13 <- `_**`string`**_

Etc/GMT-13 zone constant.

### `TZ_AmericaGrandTurk <- `_**`string`**_

America/Grand_Turk zone constant.

### `TZ_GMTAfter14 <- `_**`string`**_

Etc/GMT-14 zone constant.

### `TZ_AfricaBanjul <- `_**`string`**_

Africa/Banjul zone constant.

### `TZ_AmericaBahiaBanderas <- `_**`string`**_

America/Bahia_Banderas zone constant.

### `TZ_AtlanticFaeroe <- `_**`string`**_

Atlantic/Faeroe zone constant.

### `TZ_AntarcticaDumontDUrville <- `_**`string`**_

Antarctica/DumontDUrville zone constant.

### `TZ_GMTAfter10 <- `_**`string`**_

Etc/GMT-10 zone constant.

### `TZ_WET <- `_**`string`**_

WET zone constant.

### `TZ_GMTAfter11 <- `_**`string`**_

Etc/GMT-11 zone constant.

### `TZ_EuropeTallinn <- `_**`string`**_

Europe/Tallinn zone constant.

### `TZ_AmericaMatamoros <- `_**`string`**_

America/Matamoros zone constant.

### `TZ_AmericaStThomas <- `_**`string`**_

America/St_Thomas zone constant.

### `TZ_AustraliaAdelaide <- `_**`string`**_

Australia/Adelaide zone constant.

### `TZ_AfricaAccra <- `_**`string`**_

Africa/Accra zone constant.

### `TZ_EuropeRome <- `_**`string`**_

Europe/Rome zone constant.

### `TZ_AmericaKentuckyLouisville <- `_**`string`**_

America/Kentucky/Louisville zone constant.

### `TZ_GMT <- `_**`string`**_

Etc/GMT zone constant.

### `TZ_AmericaAntigua <- `_**`string`**_

America/Antigua zone constant.

### `TZ_SystemVYST9YDT <- `_**`string`**_

SystemV/YST9YDT zone constant.

### `TZ_AfricaJuba <- `_**`string`**_

Africa/Juba zone constant.

### `TZ_USMichigan <- `_**`string`**_

US/Michigan zone constant.

### `TZ_AmericaCayenne <- `_**`string`**_

America/Cayenne zone constant.

### `TZ_IndianMaldives <- `_**`string`**_

Indian/Maldives zone constant.

### `TZ_AmericaArgentinaSalta <- `_**`string`**_

America/Argentina/Salta zone constant.

### `TZ_AmericaPhoenix <- `_**`string`**_

America/Phoenix zone constant.

### `TZ_CET <- `_**`string`**_

CET zone constant.

### `TZ_AustraliaCanberra <- `_**`string`**_

Australia/Canberra zone constant.

### `TZ_AmericaMetlakatla <- `_**`string`**_

America/Metlakatla zone constant.

### `TZ_AustraliaPerth <- `_**`string`**_

Australia/Perth zone constant.

### `TZ_AmericaJujuy <- `_**`string`**_

America/Jujuy zone constant.

### `TZ_AfricaNiamey <- `_**`string`**_

Africa/Niamey zone constant.

### `TZ_AsiaAshkhabad <- `_**`string`**_

Asia/Ashkhabad zone constant.

### `TZ_AmericaMontevideo <- `_**`string`**_

America/Montevideo zone constant.

### `TZ_EuropeTirane <- `_**`string`**_

Europe/Tirane zone constant.

### `TZ_AsiaBahrain <- `_**`string`**_

Asia/Bahrain zone constant.

### `TZ_AsiaIstanbul <- `_**`string`**_

Asia/Istanbul zone constant.

### `TZ_AfricaBujumbura <- `_**`string`**_

Africa/Bujumbura zone constant.

### `TZ_AntarcticaSyowa <- `_**`string`**_

Antarctica/Syowa zone constant.

### `TZ_USArizona <- `_**`string`**_

US/Arizona zone constant.

### `TZ_AustraliaTasmania <- `_**`string`**_

Australia/Tasmania zone constant.

### `TZ_SystemVEST5 <- `_**`string`**_

SystemV/EST5 zone constant.

### `TZ_EuropeAndorra <- `_**`string`**_

Europe/Andorra zone constant.

### `TZ_AmericaAtka <- `_**`string`**_

America/Atka zone constant.

### `TZ_IndianReunion <- `_**`string`**_

Indian/Reunion zone constant.

### `TZ_AsiaColombo <- `_**`string`**_

Asia/Colombo zone constant.

### `TZ_USSamoa <- `_**`string`**_

US/Samoa zone constant.

### `TZ_EuropeVaduz <- `_**`string`**_

Europe/Vaduz zone constant.

### `TZ_PacificPitcairn <- `_**`string`**_

Pacific/Pitcairn zone constant.

### `TZ_AmericaMonterrey <- `_**`string`**_

America/Monterrey zone constant.

### `TZ_AfricaMbabane <- `_**`string`**_

Africa/Mbabane zone constant.

### `TZ_AsiaJakarta <- `_**`string`**_

Asia/Jakarta zone constant.

### `TZ_Zulu <- `_**`string`**_

Etc/Zulu zone constant.

### `TZ_EuropeSarajevo <- `_**`string`**_

Europe/Sarajevo zone constant.

### `TZ_AfricaMalabo <- `_**`string`**_

Africa/Malabo zone constant.

### `TZ_GMTAfter8 <- `_**`string`**_

Etc/GMT-8 zone constant.

### `TZ_GMTAfter7 <- `_**`string`**_

Etc/GMT-7 zone constant.

### `TZ_PacificMarquesas <- `_**`string`**_

Pacific/Marquesas zone constant.

### `TZ_GMTAfter9 <- `_**`string`**_

Etc/GMT-9 zone constant.

### `TZ_AmericaWinnipeg <- `_**`string`**_

America/Winnipeg zone constant.

### `TZ_EuropeSaratov <- `_**`string`**_

Europe/Saratov zone constant.

### `TZ_EuropeWarsaw <- `_**`string`**_

Europe/Warsaw zone constant.

### `TZ_MexicoBajaNorte <- `_**`string`**_

Mexico/BajaNorte zone constant.

### `TZ_SystemVPST8 <- `_**`string`**_

SystemV/PST8 zone constant.

### `TZ_AmericaStJohns <- `_**`string`**_

America/St_Johns zone constant.

### `TZ_PacificEfate <- `_**`string`**_

Pacific/Efate zone constant.

### `TZ_CanadaNewfoundland <- `_**`string`**_

Canada/Newfoundland zone constant.

### `TZ_AustraliaACT <- `_**`string`**_

Australia/ACT zone constant.

### `TZ_AmericaGrenada <- `_**`string`**_

America/Grenada zone constant.

### `TZ_EuropeKaliningrad <- `_**`string`**_

Europe/Kaliningrad zone constant.

### `TZ_AmericaElSalvador <- `_**`string`**_

America/El_Salvador zone constant.

### `TZ_GMTAfter0 <- `_**`string`**_

Etc/GMT-0 zone constant.

### `TZ_GMTAfter2 <- `_**`string`**_

Etc/GMT-2 zone constant.

### `TZ_PacificPalau <- `_**`string`**_

Pacific/Palau zone constant.

### `TZ_GMTAfter1 <- `_**`string`**_

Etc/GMT-1 zone constant.

### `TZ_GMTAfter4 <- `_**`string`**_

Etc/GMT-4 zone constant.

### `TZ_GMTAfter3 <- `_**`string`**_

Etc/GMT-3 zone constant.

### `TZ_AfricaTripoli <- `_**`string`**_

Africa/Tripoli zone constant.

### `TZ_AmericaMexicoCity <- `_**`string`**_

America/Mexico_City zone constant.

### `TZ_GMTAfter6 <- `_**`string`**_

Etc/GMT-6 zone constant.

### `TZ_GMTAfter5 <- `_**`string`**_

Etc/GMT-5 zone constant.

### `TZ_EuropeUlyanovsk <- `_**`string`**_

Europe/Ulyanovsk zone constant.

### `TZ_GMTBehind12 <- `_**`string`**_

Etc/GMT+12 zone constant.

### `TZ_AmericaArgentinaCordoba <- `_**`string`**_

America/Argentina/Cordoba zone constant.

### `TZ_CanadaPacific <- `_**`string`**_

Canada/Pacific zone constant.

### `TZ_AmericaAnchorage <- `_**`string`**_

America/Anchorage zone constant.

### `TZ_Navajo <- `_**`string`**_

Navajo zone constant.

### `TZ_AmericaWhitehorse <- `_**`string`**_

America/Whitehorse zone constant.

### `TZ_NZ <- `_**`string`**_

NZ zone constant.

### `TZ_AmericaParamaribo <- `_**`string`**_

America/Paramaribo zone constant.

### `TZ_AntarcticaSouthPole <- `_**`string`**_

Antarctica/South_Pole zone constant.

### `TZ_AntarcticaTroll <- `_**`string`**_

Antarctica/Troll zone constant.

### `TZ_AmericaRosario <- `_**`string`**_

America/Rosario zone constant.

### `TZ_AmericaRioBranco <- `_**`string`**_

America/Rio_Branco zone constant.

### `TZ_AfricaAddisAbaba <- `_**`string`**_

Africa/Addis_Ababa zone constant.

### `TZ_AsiaFamagusta <- `_**`string`**_

Asia/Famagusta zone constant.

### `TZ_PacificChatham <- `_**`string`**_

Pacific/Chatham zone constant.

### `TZ_AfricaFreetown <- `_**`string`**_

Africa/Freetown zone constant.

### `TZ_AmericaChihuahua <- `_**`string`**_

America/Chihuahua zone constant.

### `TZ_EuropeTiraspol <- `_**`string`**_

Europe/Tiraspol zone constant.

### `TZ_GMTBehind11 <- `_**`string`**_

Etc/GMT+11 zone constant.

### `TZ_GMTBehind10 <- `_**`string`**_

Etc/GMT+10 zone constant.

### `TZ_IndianMahe <- `_**`string`**_

Indian/Mahe zone constant.

### `TZ_EuropeLuxembourg <- `_**`string`**_

Europe/Luxembourg zone constant.

### `TZ_EuropeParis <- `_**`string`**_

Europe/Paris zone constant.

### `TZ_AfricaBangui <- `_**`string`**_

Africa/Bangui zone constant.

### `TZ_AmericaNewYork <- `_**`string`**_

America/New_York zone constant.

### `TZ_Iceland <- `_**`string`**_

Iceland zone constant.

### `TZ_Israel <- `_**`string`**_

Israel zone constant.

### `TZ_AmericaCreston <- `_**`string`**_

America/Creston zone constant.

### `TZ_AfricaDjibouti <- `_**`string`**_

Africa/Djibouti zone constant.

### `TZ_AfricaTunis <- `_**`string`**_

Africa/Tunis zone constant.

### `TZ_EuropeZaporozhye <- `_**`string`**_

Europe/Zaporozhye zone constant.

### `TZ_SystemVAST4ADT <- `_**`string`**_

SystemV/AST4ADT zone constant.

### `TZ_AmericaShiprock <- `_**`string`**_

America/Shiprock zone constant.

### `TZ_AmericaPanama <- `_**`string`**_

America/Panama zone constant.

### `TZ_AfricaBamako <- `_**`string`**_

Africa/Bamako zone constant.

### `TZ_AmericaStBarthelemy <- `_**`string`**_

America/St_Barthelemy zone constant.

### `TZ_AsiaVientiane <- `_**`string`**_

Asia/Vientiane zone constant.

### `TZ_EuropePrague <- `_**`string`**_

Europe/Prague zone constant.

### `TZ_AfricaGaborone <- `_**`string`**_

Africa/Gaborone zone constant.

### `TZ_EuropeVolgograd <- `_**`string`**_

Europe/Volgograd zone constant.

### `TZ_AsiaRiyadh <- `_**`string`**_

Asia/Riyadh zone constant.

### `TZ_AsiaChoibalsan <- `_**`string`**_

Asia/Choibalsan zone constant.

### `TZ_AmericaBelize <- `_**`string`**_

America/Belize zone constant.

### `TZ_IndianChristmas <- `_**`string`**_

Indian/Christmas zone constant.

### `TZ_AfricaBissau <- `_**`string`**_

Africa/Bissau zone constant.

### `TZ_PacificSamoa <- `_**`string`**_

Pacific/Samoa zone constant.

### `TZ_AmericaNorthDakotaBeulah <- `_**`string`**_

America/North_Dakota/Beulah zone constant.

### `TZ_Iran <- `_**`string`**_

Iran zone constant.

### `TZ_Singapore <- `_**`string`**_

Singapore zone constant.

### `TZ_W-SU <- `_**`string`**_

W-SU zone constant.

### `TZ_AsiaMagadan <- `_**`string`**_

Asia/Magadan zone constant.

### `TZ_EuropeBelgrade <- `_**`string`**_

Europe/Belgrade zone constant.

### `TZ_EuropeZagreb <- `_**`string`**_

Europe/Zagreb zone constant.

### `TZ_AustraliaYancowinna <- `_**`string`**_

Australia/Yancowinna zone constant.

### `TZ_AmericaBlanc-Sablon <- `_**`string`**_

America/Blanc-Sablon zone constant.

### `TZ_AfricaHarare <- `_**`string`**_

Africa/Harare zone constant.

### `TZ_AmericaMaceio <- `_**`string`**_

America/Maceio zone constant.

### `TZ_EuropeBusingen <- `_**`string`**_

Europe/Busingen zone constant.

### `TZ_PacificChuuk <- `_**`string`**_

Pacific/Chuuk zone constant.

### `TZ_AmericaRankinInlet <- `_**`string`**_

America/Rankin_Inlet zone constant.

### `TZ_Universal <- `_**`string`**_

Etc/Universal zone constant.

### `TZ_AmericaIndianaKnox <- `_**`string`**_

America/Indiana/Knox zone constant.

### `TZ_AmericaAdak <- `_**`string`**_

America/Adak zone constant.

### `TZ_AsiaThimphu <- `_**`string`**_

Asia/Thimphu zone constant.

### `TZ_AmericaDawson <- `_**`string`**_

America/Dawson zone constant.

### `TZ_SystemVAST4 <- `_**`string`**_

SystemV/AST4 zone constant.

### `TZ_AmericaThunderBay <- `_**`string`**_

America/Thunder_Bay zone constant.

### `TZ_AsiaMuscat <- `_**`string`**_

Asia/Muscat zone constant.

### `TZ_EuropeBucharest <- `_**`string`**_

Europe/Bucharest zone constant.

### `TZ_AsiaUjungPandang <- `_**`string`**_

Asia/Ujung_Pandang zone constant.

### `TZ_AmericaGuadeloupe <- `_**`string`**_

America/Guadeloupe zone constant.

### `TZ_AmericaHavana <- `_**`string`**_

America/Havana zone constant.

### `TZ_AmericaGuayaquil <- `_**`string`**_

America/Guayaquil zone constant.

### `TZ_AsiaKarachi <- `_**`string`**_

Asia/Karachi zone constant.

### `TZ_AmericaMontserrat <- `_**`string`**_

America/Montserrat zone constant.

### `TZ_AmericaLosAngeles <- `_**`string`**_

America/Los_Angeles zone constant.

### `TZ_AfricaSaoTome <- `_**`string`**_

Africa/Sao_Tome zone constant.

### `TZ_EuropeSamara <- `_**`string`**_

Europe/Samara zone constant.

### `TZ_AfricaAbidjan <- `_**`string`**_

Africa/Abidjan zone constant.

### `TZ_AsiaIrkutsk <- `_**`string`**_

Asia/Irkutsk zone constant.

### `TZ_AmericaNassau <- `_**`string`**_

America/Nassau zone constant.

### `TZ_AmericaLaPaz <- `_**`string`**_

America/La_Paz zone constant.

### `TZ_AmericaMarigot <- `_**`string`**_

America/Marigot zone constant.

### `TZ_AmericaArgentinaSanJuan <- `_**`string`**_

America/Argentina/San_Juan zone constant.

### `TZ_AsiaDamascus <- `_**`string`**_

Asia/Damascus zone constant.

### `TZ_EuropeMalta <- `_**`string`**_

Europe/Malta zone constant.

### `TZ_AfricaElAaiun <- `_**`string`**_

Africa/El_Aaiun zone constant.

### `TZ_AsiaUst-Nera <- `_**`string`**_

Asia/Ust-Nera zone constant.

### `TZ_PacificFunafuti <- `_**`string`**_

Pacific/Funafuti zone constant.

### `TZ_AfricaLibreville <- `_**`string`**_

Africa/Libreville zone constant.

### `TZ_AsiaBeirut <- `_**`string`**_

Asia/Beirut zone constant.

### `TZ_AmericaVancouver <- `_**`string`**_

America/Vancouver zone constant.

### `TZ_Poland <- `_**`string`**_

Poland zone constant.

### `TZ_IndianMauritius <- `_**`string`**_

Indian/Mauritius zone constant.

### `TZ_AmericaCiudadJuarez <- `_**`string`**_

America/Ciudad_Juarez zone constant.

### `TZ_AmericaOjinaga <- `_**`string`**_

America/Ojinaga zone constant.

### `TZ_AsiaChungking <- `_**`string`**_

Asia/Chungking zone constant.

### `TZ_EuropeGuernsey <- `_**`string`**_

Europe/Guernsey zone constant.

### `TZ_AmericaBahia <- `_**`string`**_

America/Bahia zone constant.

### `TZ_SystemVMST7MDT <- `_**`string`**_

SystemV/MST7MDT zone constant.

### `TZ_AmericaGooseBay <- `_**`string`**_

America/Goose_Bay zone constant.

### `TZ_AmericaLima <- `_**`string`**_

America/Lima zone constant.

### `TZ_AsiaKatmandu <- `_**`string`**_

Asia/Katmandu zone constant.

### `TZ_AfricaNairobi <- `_**`string`**_

Africa/Nairobi zone constant.

### `TZ_AmericaMenominee <- `_**`string`**_

America/Menominee zone constant.

### `TZ_AsiaTomsk <- `_**`string`**_

Asia/Tomsk zone constant.

### `TZ_AmericaFortaleza <- `_**`string`**_

America/Fortaleza zone constant.

### `TZ_AmericaArgentinaComodRivadavia <- `_**`string`**_

America/Argentina/ComodRivadavia zone constant.

### `TZ_PacificGuadalcanal <- `_**`string`**_

Pacific/Guadalcanal zone constant.

### `TZ_PacificPonape <- `_**`string`**_

Pacific/Ponape zone constant.

### `TZ_PacificAuckland <- `_**`string`**_

Pacific/Auckland zone constant.

### `TZ_SystemVMST7 <- `_**`string`**_

SystemV/MST7 zone constant.

### `TZ_AmericaBoaVista <- `_**`string`**_

America/Boa_Vista zone constant.

### `TZ_AsiaNicosia <- `_**`string`**_

Asia/Nicosia zone constant.

### `TZ_AmericaStKitts <- `_**`string`**_

America/St_Kitts zone constant.

### `TZ_AsiaKualaLumpur <- `_**`string`**_

Asia/Kuala_Lumpur zone constant.

### `TZ_ArcticLongyearbyen <- `_**`string`**_

Arctic/Longyearbyen zone constant.

### `TZ_AmericaDetroit <- `_**`string`**_

America/Detroit zone constant.

### `TZ_AsiaTbilisi <- `_**`string`**_

Asia/Tbilisi zone constant.

### `TZ_AfricaLusaka <- `_**`string`**_

Africa/Lusaka zone constant.

### `TZ_AmericaEirunepe <- `_**`string`**_

America/Eirunepe zone constant.

### `TZ_BrazilWest <- `_**`string`**_

Brazil/West zone constant.

### `TZ_AmericaTortola <- `_**`string`**_

America/Tortola zone constant.

### `TZ_AmericaAnguilla <- `_**`string`**_

America/Anguilla zone constant.

### `TZ_AmericaBuenosAires <- `_**`string`**_

America/Buenos_Aires zone constant.

### `TZ_AmericaBogota <- `_**`string`**_

America/Bogota zone constant.

### `TZ_AmericaThule <- `_**`string`**_

America/Thule zone constant.

### `TZ_AfricaNouakchott <- `_**`string`**_

Africa/Nouakchott zone constant.

### `TZ_AsiaAden <- `_**`string`**_

Asia/Aden zone constant.

### `TZ_ChileContinental <- `_**`string`**_

Chile/Continental zone constant.

### `TZ_AustraliaCurrie <- `_**`string`**_

Australia/Currie zone constant.

### `TZ_AsiaCalcutta <- `_**`string`**_

Asia/Calcutta zone constant.

### `TZ_AsiaHoChiMinh <- `_**`string`**_

Asia/Ho_Chi_Minh zone constant.

### `TZ_CanadaYukon <- `_**`string`**_

Canada/Yukon zone constant.

### `TZ_BrazilAcre <- `_**`string`**_

Brazil/Acre zone constant.

### `TZ_PacificNauru <- `_**`string`**_

Pacific/Nauru zone constant.

### `TZ_PacificNorfolk <- `_**`string`**_

Pacific/Norfolk zone constant.

### `TZ_EuropeRiga <- `_**`string`**_

Europe/Riga zone constant.

### `TZ_EuropeSkopje <- `_**`string`**_

Europe/Skopje zone constant.

### `TZ_AmericaRecife <- `_**`string`**_

America/Recife zone constant.

### `TZ_PacificKosrae <- `_**`string`**_

Pacific/Kosrae zone constant.

### `TZ_USIndiana-Starke <- `_**`string`**_

US/Indiana-Starke zone constant.

### `TZ_AfricaCasablanca <- `_**`string`**_

Africa/Casablanca zone constant.

### `TZ_AntarcticaMcMurdo <- `_**`string`**_

Antarctica/McMurdo zone constant.

### `TZ_AmericaDanmarkshavn <- `_**`string`**_

America/Danmarkshavn zone constant.

### `TZ_AmericaCatamarca <- `_**`string`**_

America/Catamarca zone constant.

### `TZ_MexicoGeneral <- `_**`string`**_

Mexico/General zone constant.

### `TZ_USCentral <- `_**`string`**_

US/Central zone constant.

### `TZ_AmericaManagua <- `_**`string`**_

America/Managua zone constant.

### `TZ_AmericaBarbados <- `_**`string`**_

America/Barbados zone constant.

### `TZ_AsiaPhnomPenh <- `_**`string`**_

Asia/Phnom_Penh zone constant.

### `TZ_AsiaAlmaty <- `_**`string`**_

Asia/Almaty zone constant.

### `TZ_AmericaBelem <- `_**`string`**_

America/Belem zone constant.
