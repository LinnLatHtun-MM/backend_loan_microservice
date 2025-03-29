package per.llt.loan.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.llt.loan.constants.LoanConstants;
import per.llt.loan.dto.LoansDto;
import per.llt.loan.entity.Loans;
import per.llt.loan.exception.LoansAlreadyException;
import per.llt.loan.exception.ResourceNotFoundException;
import per.llt.loan.mapper.LoansMapper;
import per.llt.loan.repo.LoansRepository;
import per.llt.loan.service.ILoansService;

import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class LoansServiceImpl implements ILoansService {

    @Autowired
    private LoansRepository loansRepository;

    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    @Override
    public void createLoan(String mobileNumber) {
        Optional<Loans> loanNumber = loansRepository.findByMobileNumber(mobileNumber);
        if (loanNumber.isPresent()) {
            throw new LoansAlreadyException("Loan already registered with given mobile number: " + mobileNumber);
        }
        loansRepository.save(createNewLoans(mobileNumber));
    }


    private Loans createNewLoans(String mobileNumber) {
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        Loans newLoan = new Loans();
        newLoan.setLoanNumber(String.valueOf(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoanConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoanConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoanConstants.NEW_LOAN_LIMIT);
        return newLoan;
    }

    @Override
    public LoansDto fetchLoan(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber));
        return LoansMapper.mapToLoansDto(loans, new LoansDto());
    }

    @Override
    public boolean updateLoan(LoansDto loansDto) {
        boolean isUpdated = false;
        if (loansDto != null) {
            Loans loans = loansRepository.findByLoanNumber(loansDto.getLoanNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Loan", "LoanNumber", loansDto.getLoanNumber()));
            LoansMapper.mapToLoans(loansDto, loans);
            loansRepository.save(loans);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteLoan(String mobileNumber) {
        boolean isDeleted = false;
        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber));
        try {
            loansRepository.deleteById(loans.getLoanId());
            isDeleted = true;
        } catch (Exception e) {
            isDeleted = false;
        }
        return isDeleted;
    }
}
