package aviasales.ticket.management.config;

import com.atomikos.icatch.jta.UserTransactionManager;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;
import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;

public class AtomikosJtaPlatform extends AbstractJtaPlatform {
    private static final long serialVersionUID = 1L;

    private static final TransactionManager TRANSACTION_MANAGER = new UserTransactionManager();
    private static final UserTransaction USER_TRANSACTION = new com.atomikos.icatch.jta.UserTransactionImp();

    @Override
    protected TransactionManager locateTransactionManager() {
        return TRANSACTION_MANAGER;
    }

    @Override
    protected UserTransaction locateUserTransaction() {
        return USER_TRANSACTION;
    }
}