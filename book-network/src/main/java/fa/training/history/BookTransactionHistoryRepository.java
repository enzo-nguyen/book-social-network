package fa.training.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Integer> {
    @Query("""
            SELECT bth
            FROM BookTransactionHistory bth
            WHERE bth.user.id = :userId
        """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);

    @Query("""
            SELECT bth
            FROM BookTransactionHistory bth
            WHERE bth.book.owner.id = :userId
        """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Integer userId);

    @Query("""
            SELECT (COUNT(*) > 0) AS isBorrowed
            FROM BookTransactionHistory bth
            WHERE bth.user.id = :userId
            AND bth.book.id = :bookId
            AND bth.returnApproved = false
        """)
    boolean isAlreadyBorrowedByUser(Integer bookId, Integer userId);

    @Query("""
            SELECT bth
            FROM BookTransactionHistory bth
            WHERE bth.user.id = :userId
            AND bth.book.id = :bookId
            AND bth.returned = false
            AND bth.returnApproved = false
        """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(Integer bookId, Integer userId);

    @Query("""
            SELECT bth
            FROM BookTransactionHistory bth
            WHERE bth.book.owner.id = :userId
            AND bth.book.id = :bookId
            AND bth.returned = true
            AND bth.returnApproved = false
        """)
    Optional<BookTransactionHistory> findByBookIdAndOwnerId(Integer bookId, Integer userId);
}
