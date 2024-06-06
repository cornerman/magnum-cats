package com.augustnagro.magnum.ce

import cats.implicits.*
import cats.effect.{Resource, Sync}
import com.augustnagro.magnum.{DbCon, DbTx, SqlException}

import java.sql.Connection
import javax.sql.DataSource

inline def connectF[F[_]: Sync] = new PartiallyAppliedConnectF

inline def transactF[F[_]: Sync] = new PartiallyAppliedTransactF

class PartiallyAppliedConnectF[F[_]: Sync] {
  def apply[F[_]: Sync, T](dataSource: DataSource)(f: DbCon ?=> F[T]): F[T] =
    val connection = Resource.fromAutoCloseable(Sync[F].blocking(dataSource.getConnection()))
    connection.use(con => f(using DbCon(con)))
}

class PartiallyAppliedTransactF[F[_]: Sync] {
  def apply[T](dataSource: DataSource, connectionConfig: Connection => F[Unit])(f: DbTx ?=> F[T]): F[T] =
    val connection = Resource.fromAutoCloseable(Sync[F].blocking(dataSource.getConnection()))
    connection.use { con =>
      connectionConfig(con) *>
        Sync[F].delay(con.setAutoCommit(false)) *>
        f(using DbTx(con)).flatTap(_ => Sync[F].blocking(con.commit())).onError(_ => Sync[F].blocking(con.rollback()))
    }

  def apply[T](dataSource: DataSource)(f: DbTx ?=> F[T]): F[T] = apply(dataSource, _ => Sync[F].unit)(f)
}
