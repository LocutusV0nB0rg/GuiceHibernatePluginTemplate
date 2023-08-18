package borg.locutus.guicehibernateplugin.entities;

import com.google.inject.Inject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.hibernate.IdentifierLoadAccess;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Repository<T, S extends Serializable> {
    private final Class<T> type;

    private final SessionFactory factory;

    public Session openSession() {
        return this.factory.openSession();
    }

    public CompletableFuture<Void> saveOrUpdate(T value) {
        if (value == null) {
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.runAsync(() -> {
            try (Session session = this.openSession()) {
                final Transaction transaction = session.beginTransaction();
                transaction.begin();
                session.saveOrUpdate(value);
                transaction.commit();
            }
        });
    }

    public CompletableFuture<Void> delete(T value) {
        if (value == null) {
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.runAsync(() -> {
            try (Session session = this.openSession()) {
                final Transaction transaction = session.getTransaction();
                transaction.begin();
                session.delete(value);
                transaction.commit();
            }
        });
    }

    public CompletableFuture<T> findById(S id) {
        if (id == null) {
            return CompletableFuture.completedFuture(null);
        }

        CompletableFuture<T> objectCompletableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try (Session session = this.openSession()) {
                IdentifierLoadAccess<T> teacherIdentifierLoadAccess = session.byId(type);
                objectCompletableFuture.complete(teacherIdentifierLoadAccess.load(id));
            } catch (Throwable t) {
                objectCompletableFuture.completeExceptionally(t);
            }
        });
        return objectCompletableFuture;
    }
}
