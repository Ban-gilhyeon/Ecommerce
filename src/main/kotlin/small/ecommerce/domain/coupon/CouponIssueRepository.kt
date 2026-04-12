package small.ecommerce.domain.coupon

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CouponIssueRepository : JpaRepository<CouponIssue, Long>
