package com.augustnagro.magnum.ce

import cats.effect.Sync
import com.augustnagro.magnum.{BatchUpdateResult, DbCon, ImmutableRepo, Repo, RepoDefaults, Spec}

open class ImmutableRepoF[F[_]: Sync, E, ID](inner: ImmutableRepo[E, ID]) {
  def count(using DbCon): F[Long] = Sync[F].blocking(inner.count)
  def existsById(id: ID)(using DbCon): F[Boolean] = Sync[F].blocking(inner.existsById(id))
  def findAll(using DbCon): F[Vector[E]] = Sync[F].blocking(inner.findAll)
  def findAll(spec: Spec[E])(using DbCon): F[Vector[E]] = Sync[F].blocking(inner.findAll(spec))
  def findById(id: ID)(using DbCon): F[Option[E]] = Sync[F].blocking(inner.findById(id))
  def findAllById(ids: Iterable[ID])(using DbCon): F[Vector[E]] = Sync[F].blocking(inner.findAllById(ids))
}
object ImmutableRepoF {
  def apply[F[_] : Sync, E, ID](using defaults: RepoDefaults[?, E, ID]): ImmutableRepoF[F, E, ID] =
    new ImmutableRepoF(ImmutableRepo[E, ID])
}

open class RepoF[F[_]: Sync, EC, E, ID](inner: Repo[EC, E, ID]) extends ImmutableRepoF[F, E, ID](inner) {
  def delete(entity: E)(using DbCon): F[Unit] = Sync[F].blocking(inner.delete(entity))
  def deleteById(id: ID)(using DbCon): F[Unit] = Sync[F].blocking(inner.deleteById(id))
  def truncate()(using DbCon): F[Unit] = Sync[F].blocking(inner.truncate())
  def deleteAll(entities: Iterable[E])(using DbCon): F[BatchUpdateResult] = Sync[F].blocking(inner.deleteAll(entities))
  def deleteAllById(ids: Iterable[ID])(using DbCon): F[BatchUpdateResult] = Sync[F].blocking(inner.deleteAllById(ids))
  def insert(entityCreator: EC)(using DbCon): F[Unit] = Sync[F].blocking(inner.insert(entityCreator))
  def insertAll(entityCreators: Iterable[EC])(using DbCon): F[Unit] = Sync[F].blocking(inner.insertAll(entityCreators))
  def insertReturning(entityCreator: EC)(using DbCon): F[E] = Sync[F].blocking(inner.insertReturning(entityCreator))
  def insertAllReturning(entityCreators: Iterable[EC])(using DbCon): F[Vector[E]] = Sync[F].blocking(inner.insertAllReturning(entityCreators))
  def update(entity: E)(using DbCon): F[Unit] = Sync[F].blocking(inner.update(entity))
  def updateAll(entities: Iterable[E])(using DbCon): F[BatchUpdateResult] = Sync[F].blocking(inner.updateAll(entities))
}
object RepoF {
  def apply[F[_]: Sync, EC, E, ID](using defaults: RepoDefaults[EC, E, ID]): RepoF[F, EC, E, ID] =
    new RepoF(Repo[EC, E, ID])
}