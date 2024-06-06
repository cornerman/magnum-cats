package com.augustnagro.magnum.ce

import cats.effect.Sync
import com.augustnagro.magnum.*

open class ImmutableRepoF[F[_]: Sync, E, ID](using defaults: RepoDefaults[?, E, ID]) {
  def count(using DbCon): F[Long] = Sync[F].blocking(defaults.count)
  def existsById(id: ID)(using DbCon): F[Boolean] = Sync[F].blocking(defaults.existsById(id))
  def findAll(using DbCon): F[Vector[E]] = Sync[F].blocking(defaults.findAll)
  def findAll(spec: Spec[E])(using DbCon): F[Vector[E]] = Sync[F].blocking(defaults.findAll(spec))
  def findById(id: ID)(using DbCon): F[Option[E]] = Sync[F].blocking(defaults.findById(id))
  def findAllById(ids: Iterable[ID])(using DbCon): F[Vector[E]] = Sync[F].blocking(defaults.findAllById(ids))
}

open class RepoF[F[_]: Sync, EC, E, ID](using defaults: RepoDefaults[EC, E, ID]) extends ImmutableRepoF[F, E, ID] {
  def delete(entity: E)(using DbCon): F[Unit] = Sync[F].blocking(defaults.delete(entity))
  def deleteById(id: ID)(using DbCon): F[Unit] = Sync[F].blocking(defaults.deleteById(id))
  def truncate()(using DbCon): F[Unit] = Sync[F].blocking(defaults.truncate())
  def deleteAll(entities: Iterable[E])(using DbCon): F[BatchUpdateResult] = Sync[F].blocking(defaults.deleteAll(entities))
  def deleteAllById(ids: Iterable[ID])(using DbCon): F[BatchUpdateResult] = Sync[F].blocking(defaults.deleteAllById(ids))
  def insert(entityCreator: EC)(using DbCon): F[Unit] = Sync[F].blocking(defaults.insert(entityCreator))
  def insertAll(entityCreators: Iterable[EC])(using DbCon): F[Unit] = Sync[F].blocking(defaults.insertAll(entityCreators))
  def insertReturning(entityCreator: EC)(using DbCon): F[E] = Sync[F].blocking(defaults.insertReturning(entityCreator))
  def insertAllReturning(entityCreators: Iterable[EC])(using DbCon): F[Vector[E]] = Sync[F].blocking(defaults.insertAllReturning(entityCreators))
  def update(entity: E)(using DbCon): F[Unit] = Sync[F].blocking(defaults.update(entity))
  def updateAll(entities: Iterable[E])(using DbCon): F[BatchUpdateResult] = Sync[F].blocking(defaults.updateAll(entities))
}
