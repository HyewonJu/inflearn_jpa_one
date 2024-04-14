package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    // 상품 등록
    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);   // 신규 상품 등록
        } else {
            em.merge(item); // 병합(merge)

            /**
             * 여기서,
             * item :: 준영속 상태
             * em.merge() 리턴값 :: 영속 상태
             * --> 따라서 em.merge() 리턴값을 이용하여 사용해서 추후에 값 변경하는 것이 좋음
             */
        }
    }

    // 상품 조회
    public Item findOne(Long itemId) {
        return em.find(Item.class, itemId);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
