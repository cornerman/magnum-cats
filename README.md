# magnum-cats

A simple extension to facilitate working with cats-effect and the jdbc-based database library [magnum](https://github.com/AugustNagro/magnum).

## Usage

```scala
libraryDependencies += "com.github.cornerman" %% "magnum-cats-effect" % "0.1.0"
```

Queries:
```scala
import cats.effect.IO
import com.augustnagro.magnum.*
import com.augustnagro.magnum.ce.*

connectF[IO](ds):
  sql"SELECT 1".query[Int].runF[IO]: IO[Int]

transactF[IO](ds):
  sql"UPDATE world SET broken=false".update.runF[IO]: IO[Int]

transactF[IO](ds):
  sql"INSERT INTO world(message) VALUES ('hallo') returning message".returning[String].runF[IO]: IO[String]
```

Repositories:
```scala
import com.augustnagro.magnum.ce.{RepoF, ImmutableRepoF}

val MyRepo = RepoF[IO, Person.Creator, Person, Person.Id]
val MyImmutableRepo = ImmutableRepoF[IO, Person, Person.Id]

MyRepo.insert(...): IO[Unit]
MyImmutableRepo.findAll: IO[Vector[Person]]
```
