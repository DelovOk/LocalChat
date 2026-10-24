package ru.Novoselov_Leonid.LocalChat.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import ru.Novoselov_Leonid.LocalChat.entity.RoomMember;
import ru.Novoselov_Leonid.LocalChat.entity.User;
import ru.Novoselov_Leonid.LocalChat.enums.UserStatus;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomQueryDaoImpl implements CustomQueryDao {
    private final EntityManager entityManager;

    public CustomQueryDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<RoomMember> findMemberByRoomIdAndUserId(Long roomId, Long userId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RoomMember> criteriaQuery = criteriaBuilder.createQuery(RoomMember.class);

        Root<RoomMember> root = criteriaQuery.from(RoomMember.class);
        Predicate roomPredicate = criteriaBuilder.equal(root.get("room").get("id"), roomId);
        Predicate userPredicate = criteriaBuilder.equal(root.get("user").get("id"), userId);

        criteriaQuery.select(root).where(criteriaBuilder.and(roomPredicate, userPredicate));

        try {
            return Optional.of(entityManager.createQuery(criteriaQuery).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findUsersByProfileStatus(UserStatus status) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

        Root<User> root = criteriaQuery.from(User.class);
        Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), status);

        criteriaQuery.select(root).where(statusPredicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
