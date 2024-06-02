package com.augustnagro.magnum.ce

import cats.implicits.*
import cats.effect.{Resource, Sync}
import com.augustnagro.magnum.{DbCon, DbTx, SqlException}

import java.sql.Connection
import javax.sql.DataSource

def connectF[F[_]: Sync, T](dataSource: DataSource)(f: DbCon ?=> F[T]): F[T] =
  val connection = Resource.fromAutoCloseable(Sync[F].blocking(dataSource.getConnection()))
  connection.use(con => f(using DbCon(con)))

def transactF[F[_]: Sync, T](dataSource: DataSource, connectionConfig: Connection => F[Unit])(f: DbTx ?=> F[T]): F[T] =
  val connection = Resource.fromAutoCloseable(Sync[F].blocking(dataSource.getConnection()))
  connection.use { con =>
    connectionConfig(con) *>
      Sync[F].delay(con.setAutoCommit(false)) *>
      f(using DbTx(con)).flatTap(_ => Sync[F].blocking(con.commit())).onError(_ => Sync[F].blocking(con.rollback()))
  }

def transactF[F[_]: Sync, T](dataSource: DataSource)(f: DbTx ?=> F[T]): F[T] =
  transactF(dataSource, _ => Sync[F].unit)(f)
