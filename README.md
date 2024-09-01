# Task 1 : TEST-OPERATE
Download the repo and check everything works as expected. Play just a bit with ScalaCheck and see which parameters it has (e.g. number of
generated tests?). Play also with ScalaTest, and see if it can perform parameterized tests. What are key differences between the two?

## Implementazione:
ScalaTest e ScalaCheck sono due librerie di testing molto utilizzate nell'ecosistema Scala, ma seguono approcci differenti. Per chiarire meglio le loro differenze, confronteremo due classi di esempio: `StreamCheck` ([StreamCheck](src/test/scala/u04/datastructures/StreamCheck.scala)), che utilizza ScalaCheck, e `StreamTest` ([StreamTest](src/test/scala/u04/datastructures/StreamTest.scala)), che utilizza ScalaTest.

## ScalaCheck (`StreamCheck`)
ScalaCheck si concentra sul *Property-Based Testing*, dove l'idea principale è quella di definire proprietà generali che il codice deve rispettare. ScalaCheck genera automaticamente input casuali per verificare che queste proprietà siano sempre valide.

### Esempi:

- **Generazione di Dati**: ScalaCheck utilizza generatori per creare automaticamente valori casuali. Nel codice di esempio, il metodo `genStream[A]` genera stream di interi casuali di lunghezza variabile.
- **Definizione delle Proprietà**: Le proprietà del codice vengono definite attraverso il metodo `property`. Ad esempio, la proprietà `"length is non-negative"` verifica che la lunghezza di uno stream sia sempre non negativa, indipendentemente dai dati generati.
- **Validazione delle Proprietà**: ScalaCheck esegue test su un ampio range di valori casuali per ogni proprietà definita, garantendo che il comportamento del codice sia corretto in vari scenari.

## ScalaTest (`StreamTest`)

ScalaTest, invece, è una libreria di *Unit Testing* tradizionale che permette di scrivere test specifici e dettagliati, dove ogni test verifica una singola funzione o comportamento del codice.

### Esempi:

- **Test Specifici**: In ScalaTest, i test sono definiti manualmente per ogni singolo caso. Ad esempio, il test `"Stream length is non-negative"` verifica che la lunghezza di uno stream generato casualmente sia non negativa. 
- **Esecuzione Multipla**: Per simulare diversi input, ScalaTest può eseguire lo stesso test più volte con dati differenti (ad esempio, generando 10 stream casuali diversi), ma i dati sono generati e gestiti manualmente.
- **Asserzioni Esplicite**: Le asserzioni (`assert`) vengono utilizzate per verificare condizioni specifiche all'interno del test, come l'uguaglianza tra la somma originale e quella filtrata nello stream.

## Confronto

- **Approccio al Testing**:
  - *ScalaCheck*: Si basa su proprietà generali e genera input casuali per testare queste proprietà su un vasto insieme di casi.
  - *ScalaTest*: Si focalizza su test specifici e dettagliati per casi particolari, con input gestiti manualmente.

- **Flessibilità dei Dati di Input**:
  - *ScalaCheck*: Automatizza la generazione di dati di input, esplorando un range più ampio di casi possibili.
  - *ScalaTest*: Richiede la definizione manuale degli input o la simulazione tramite codice specifico.

- **Scopo del Test**:
  - *ScalaCheck*: Ideale per verificare proprietà generali e invarianti nel tempo su un ampio numero di scenari.
  - *ScalaTest*: Adatto per test più specifici, dove è necessaria una verifica dettagliata e mirata di singole funzioni o comportamenti.

## Conclusione

ScalaCheck e ScalaTest offrono approcci complementari al testing in Scala. ScalaCheck è particolarmente potente per validare proprietà generali attraverso la generazione automatica di dati, mentre ScalaTest è più adatto per test dettagliati e specifici. La scelta tra i due dipende dal tipo di verifica che si intende effettuare: proprietà generali e robustezza del codice con ScalaCheck, oppure test dettagliati e mirati con ScalaTest.

# Task 2: ADT-VERIFIER
Define an ADT for sequences with some operations: map/filer/flatMap/fold/reduce/.... Turn into a Scala trait, and define ScalaCheck
properties capturing axioms 1-to-1. Develop two implementations (Cons/Nil and by Scala List). Engineer tests such that you can easily show they
both satisfy those properties.

## Implementazione:

L'ADT (Abstract Data Type) per sequences è stato implementato in due modi diversi: utilizzando una struttura personalizzata basata su `Cons` e `Nil`, e sfruttando le liste di Scala tramite un alias "opaco".

### 1. Implementazione Personalizzata: [CustomSequence](src/main/scala/u04/adts/CustomSequence.scala)

L'implementazione di `CustomSequence` utilizza un tipo di dato enumerato (`enum`) per rappresentare una sequence, con due casi principali:

- **`Cons(head, tail)`**: rappresenta un elemento (`head`) seguito da un'altra sequence (`tail`).
- **`Nil()`**: rappresenta una sequence vuota.

Questa implementazione segue un approccio classico per definire liste concatenate, simile a una lista collegata. Offre una serie di operazioni come `map`, `filter`, `flatMap`, `foldLeft`, `foldRight` e `concat`, definite tramite pattern matching.

### 2. Implementazione basata su Liste Scala: [NewListSequence](src/main/scala/u04/adts/NewListSequence.scala)

L'altra implementazione, `NewListSequence`, è un'astrazione più sottile sopra le liste di Scala (`List`). Qui, viene utilizzato un alias opaco (`opaque type`) per rappresentare l'ADT delle sequences, mappato direttamente su una lista Scala. Anche in questo caso, sono disponibili operazioni simili (`map`, `filter`, `flatMap`, `foldLeft`, `foldRight`, `concat`), che però sono semplicemente delegate ai metodi nativi di `List`.

Questa soluzione offre il vantaggio di sfruttare l'implementazione ottimizzata delle liste di Scala, mantenendo al contempo l'interfaccia dell'ADT astratta.

## Testing dell'ADT con ScalaCheck

Il codice di testing visibile in [SequenceProperties](src/test/scala/u04/datastructures/Task2/SequenceProperties.scala) utilizza ScalaCheck per verificare che le implementazioni rispettino determinati assiomi attraverso il *property-based testing*, generando automaticamente una vasta gamma di dati di input.

### Operazioni Testate e Assiomi

1. **Operazione `map`**
    - **Assioma 1**: `map(nil, f) = nil`
    - **Assioma 2**: `map(cons(h, t), f) = cons(f(h), map(t, f))`

   I test `map_nil` e `map_cons` verificano che la mappatura di una funzione su una sequence vuota ritorni una sequence vuota, e che la mappatura su una sequence non vuota applichi correttamente la funzione a ogni elemento.

2. **Operazione `filter`**
    - **Assioma 1**: `filter(nil, p) = nil`
    - **Assioma 2**: `filter(cons(h, t), p) = if (p(h)) cons(h, filter(t, p)) else filter(t, p)`

   I test `filter_nil` e `filter_cons` assicurano che il filtraggio su una sequence vuota ritorni una sequence vuota, e che su una sequence non vuota, gli elementi siano filtrati correttamente in base al predicato.

3. **Operazione `flatMap`**
    - **Assioma 1**: `flatMap(nil, f) = nil`
    - **Assioma 2**: `flatMap(cons(h, t), f) = concat(f(h), flatMap(t, f))`

   I test `flatMap_nil` e `flatMap_cons` controllano che il `flatMap` su una sequence vuota produca una sequence vuota, e che su una sequence non vuota, la concatenazione delle sequence mappate sia corretta.

4. **Operazione `fold`**
    - **Assioma 1**: `fold(nil, z)(f) = z`
    - **Assioma 2**: `fold(cons(h, t), z)(f) = fold(t, f(z, h))`

   I test `foldLeft_nil`, `foldLeft_cons`, `foldRight_nil` e `foldRight_cons` verificano il corretto comportamento delle operazioni di folding sia a sinistra (`foldLeft`) che a destra (`foldRight`), rispettando gli assiomi.

5. **Operazione `concat`**
    - **Assioma 1**: `concat(nil, l) = l`
    - **Assioma 2**: `concat(cons(h, t), l) = cons(h, concat(t, l))`

   I test `concat_nil` e `concat_cons` validano che la concatenazione con una sequence vuota restituisca la sequence originale, mentre la concatenazione di due sequence non vuote produce una nuova sequence correttamente concatenata.


# Task 3: MONAD-VERIFIER
Define ScalaCheck properties for Monad axioms, and prove that some of the monads given during the lesson actually satisfy them. Derive a
general methodology to structure those tests.

## Implementazione:

Il codice presentato utilizza **ScalaCheck** per testare le proprietà fondamentali delle monadi. Esse sono strutture matematiche utilizzate per rappresentare computazioni, e devono rispettare tre leggi fondamentali: identità a sinistra, identità a destra e associatività. Pertanto vi è una prima fase del file di test [MonadProperties](src/test/scala/u04/datastructures/Task3/MonadProperties.scala) dove si testano queste proprietà. 

Successivamente si è preso come esempio la monade Optionals utilizzata anche a lezione (implementata nello stesso modulo) e si sono verificate le tre proprietà anche su di essa, garantendo la sua implementazione come monade.

# Task 4: MVC-ENGINEER
Start with the given MVC monadic application: extend it to realise a more complex application, e.g. the DrawNumberGame. Of course, be fully
monadic. Up to which complexity can one reach? Could we come up with a simple MVC application with reactive GUI, and/or could a GameLoop
be framed into a fully monadic application?

## Implementazione:


All'interno del file [DrawNumberGame](src/main/scala/u04/monads/Task4/DrawNumberGame.scala). Il codice fornito implementa un semplice gioco in stile "indovina il numero" utilizzando il pattern Model-View-Controller (MVC) in un contesto completamente monadico. L'applicazione utilizza monadi per gestire lo stato del gioco e l'interfaccia grafica (GUI) reattiva, integrando concetti di programmazione funzionale con la gestione degli stati e degli eventi. Questo approccio dimostra la potenza e la flessibilità delle monadi nel costruire applicazioni interattive, che vanno oltre semplici operazioni su dati.

## Struttura del Gioco

Il gioco consiste nel generare un numero casuale tra 1 e 100, che l'utente deve indovinare attraverso un'interfaccia grafica. Il codice è suddiviso in diverse componenti, ciascuna gestita monadicamente:

### 1. **Gestione dello Stato del Gioco (`DrawNumberState`)**
Il trait `DrawNumberState` definisce le operazioni per gestire il numero casuale da indovinare, offrendo metodi per generare un nuovo numero (`drawNewNumber`) e per verificare l'ipotesi dell'utente (`checkGuess`). Queste operazioni sono modellate come monadi di stato (`State[Int, A]`), permettendo la gestione del flusso di dati all'interno del gioco.

### 2. **Setup dell'Interfaccia Grafica (`windowSetup`)**
La funzione `windowSetup` utilizza un costrutto monadico per configurare la finestra del gioco, aggiungendo pulsanti, campi di testo e etichette. La finestra è parte dello stato che viene gestito attraverso la monade `State[Window, A]`, consentendo di mantenere l'interfaccia reattiva ai cambiamenti di stato.

### 3. **Trasformazioni dello Stato**
Per integrare gli stati `State[Window, A]` e `State[Int, A]` in un unico contesto, sono stati definiti degli `extension methods` che permettono di trasformare questi stati in `State[(Window, Int), A]`. Questo approccio monadico consente di gestire simultaneamente sia lo stato del gioco (numero da indovinare) sia l'interfaccia utente.

## Considerazioni

L'approccio monadico utilizzato qui può essere ulteriormente sviluppato per integrare un **Game Loop** più complesso in un'applicazione completamente monadica, ad esempio in giochi che richiedono la gestione di animazioni o il controllo del tempo di gioco.

Sebbene questo approccio sia potente, la complessità dell'applicazione potrebbe diventare difficile da gestire man mano che si aggiungono funzionalità, specialmente quando si tratta di sincronizzare stati complessi o di gestire performance in applicazioni grafiche reattive. Tuttavia, la modularità e la chiarezza che le monadi offrono nella gestione degli stati e delle operazioni sequenziali potrebbero bilanciare questi aspetti, rendendo comunque il modello scalabile per applicazioni di media complessità.

# Task 5: ADVANCED-FP-LLM
LLMs/ChatGPT can arguably help in write/improve/complete/implement/reverse-engineer ADT specifications, ADTs in Scala, and monadic
specifications. Check if/whether this is the case.

# Implementazione:

Per svolgere questo task è stato utilizzato interamente ChatGPT, ora analizziamo singolarmente le varie operazioni del task:

* **write** : nella scrittura di specifiche ADT risulta a mio parere molto poco allenato. Su 10 soluzioni richieste corrette sono state solo 2. Utilizzando tecniche zero-shot è estremamente inaccurato, mentre con tecniche few-shots riportando per esempio il codice in "SequenceADT" capisce meglio cosa deve generare. La classe [NewSequenceADT](src/main/scala/u04/adts/NewSequenceADT.scala) è uno dei risultati forniti, funzionante... ma senza nessuna utilità.
* **improve**: GPT è sempre capace di migliorare anche se di poco il codice fornito. L'importante è dargli sempre un esempio all'interno del prompt da cui partire. Altrimenti, se lasciato libero di generare qualsiasi cosa produce del codice di qualità molto bassa.
* **complete**: Sì, ChatGPT può completare specifiche di ADT parzialmente definite, basandosi sui costrutti già esistenti nel codice e proponendo elementi mancanti. Ad esempio, se è definito solo un sottoinsieme di costruttori in un ADT, ChatGPT può suggerire i costruttori rimanenti e le relative operazioni:
  ```Scala
  sealed trait ConfigOption
    case class BooleanOption(value: Boolean) extends ConfigOption
    // ChatGPT potrebbe suggerire di completare con:
    case class StringOption(value: String) extends ConfigOption
    case class IntOption(value: Int) extends ConfigOption

  ```
* **implement**: Sì, ChatGPT è in grado di implementare specifiche monadiche, generando codici che rispettano le leggi monadiche (identità sinistra, identità destra, e associatività). Un esempio semplice potrebbe essere l'implementazione di una monade Option in Scala:

    ```Scala
      trait Monad[F[_]]:
      def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
      def unit[A](a: => A): F[A]
    
      given optionMonad: Monad[Option] with
      def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] =
       fa match
        case Some(value) => f(value)
        case None => None
    
      def unit[A](a: => A): Option[A] = Some(a)
        
    ```

* **reverse-engineer**: Sì, ChatGPT può assistere nel reverse engineering di specifiche esistenti, analizzando il codice per dedurre le specifiche implicite o non documentate. Per esempio, dato un'implementazione di una monade, ChatGPT può inferire quali leggi monadiche sono rispettate e proporre test per verificarne la conformità. Ad esempio, se dato un metodo flatMap e unit, ChatGPT può suggerire un test di proprietà:
     ```Scala
        property("left_identity") = forAll { (x: Int, f: Int => Option[Int]) =>
          optionMonad.flatMap(optionMonad.unit(x))(f) == f(x)
        }
     ```
ChatGPT si è dimostrato un alleato nell'esecuzione dei task, ma non si può fare pienamente affidamento alle sue capacità in quando specialmente in ambito monade richiede obbligatoriamente degli esempi di codice per poter avere una possibilità di analizzare codici complessi come quello MVC implementato. Inoltre, cosa che ho notato in questo modulo... la modularità è nemica di ChatGPT, gli import in Scala lo mettono seriamente in difficoltà nel riconoscere i contesti. Se non gli si fornisce il codice di ogni modulo utilizzato lui lo "assume", ma questo aumenta il rischio di errore o a volte fa generare dei codici con metodi che non esistono da nessuna parte. 