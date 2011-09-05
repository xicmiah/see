package see.integration;

import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import see.See;
import see.parser.config.ConfigBuilder;
import see.parser.config.GrammarConfiguration;
import see.parser.config.LocalizedBigDecimalFactory;
import see.parser.numbers.NumberFactory;
import see.tree.Node;

import java.util.Locale;

import static org.junit.Assert.assertNotNull;

@RunWith(Theories.class)
public class ProductionFormulasTest {

    See see;

    @Before
    public void setUp() throws Exception {
        Locale locale = new Locale("ru", "RU");
        NumberFactory numberFactory = new LocalizedBigDecimalFactory(locale);
        GrammarConfiguration config = ConfigBuilder.defaultConfig().setNumberFactory(numberFactory).build();

        see = new See(config);
    }

    @DataPoints
    public static final String[] data = {
            "RfC2=NULL;\rTempRating=PDRate;\rM=max(0;0,02*(19-TempRating));\rL=min(0,2;M);\rIndex1=index(\"Type_and_Source_of_Appraisal\";AssessSource;AssessType);\rIndex2=index(\"Value_and_Liquidity\";ExpertRate;BusinessValue);\rif (L*Index1*Index2 > 1) \r      then {RfC2=1;}\r      else {RfC2=L*Index1*Index2;}\rreturn RfC2\r",
            "return LoanSum",
            "Y=\"Adjustment\";\rX=\"90\";\rELMultiplier=index(\"EL_Adjustment_Days_to_Maturity\";Y;X);\rEL=PD*EAD*LGD*ELMultiplier/10000;\rreturn EL",
            "return 0;",
            "TotalCollValueLGD = Sigma(CollValueLGD);\rreturn TotalCollValueLGD",
            "CurrentOutstanding = LoanSum;\rInterest = Rate/100;\rEAD=CurrentOutstanding+CurrentOutstanding*Interest/12;\rreturn EAD",
            "A1=index(\"Value_and_Liquidity\";ExpertRate;BusinessValue);\rA2=index(\"Type_and_Source_of_Appraisal\";AssessSource;AssessType);\rY=0,1+max(0;0,05*(19-PDRate));\rX=min(0,8;Y)*A1*A2;\rReturnRate= min(1;X);\rreturn ReturnRate*100",
            "if(LoanMode!=\"Loan\"&&LoanMode!=\"Guarantee\"&&LoanMode!=\"Unpaid_LC\"&&LoanMode!=\"Nonrevolving_CL\"&&LoanMode!=\"Revolving_CL\"&&LoanMode!=\"Overdraft\"&&LoanMode!=\"Loan_GA\"&&LoanMode!=\"Nonrevolving_CL_GA\"&&LoanMode!=\"Revolving_CL_GA\"&&LoanMode!=\"Precious_Metals_Loan\") then {fail (1;\"Неизвестный Режим кредитования\");}\rreturn NULL",
            "CollValueLGD=ReturnRate*AssessmentOut/100;\rreturn CollValueLGD\r",
            "CurrentOutstanding = Debt;\rInterest = Rate/100;\rEAD=CurrentOutstanding+CurrentOutstanding*Interest/4;\rreturn EAD",
            "daysToMaturity=delta(AgrEndDate;reportDate);\rif (daysToMaturity < 0) then {\r   fail(1;\"Дата завершения договора не может быть меньше текущей даты\");}\rreturn daysToMaturity;",
            "DiscountRate=100-ReturnRate;\rreturn DiscountRate",
            "Limit_Logic=\"Limit_Logic\";\rRegime=index(\"Regime_Limit_Logic\";Limit_Logic;LoanMode);\rreturn Regime",
            "if (isDefined(LoanSum)==0) then {\rfail(1;\"Для данного режима кредитования не указан 'Размер обязательств/сумма первой выборки'\");};\rCurrentOutstanding = LoanSum;\rInterest = Rate/100;\rEAD=CurrentOutstanding+CurrentOutstanding*Interest/12;\rreturn EAD",
            "A1=index(\"Value_and_Liquidity\";ExpertRate;BusinessValue);\rA2=index(\"Type_and_Source_of_Appraisal\";AssessSource;AssessType);\rreturn 0,1*A1*A2*100",
            "RfC2=TempRating=M=0;\rL=min(0,8;0,1+M);\rIndex1=index(\"Type_and_Source_of_Appraisal\";AssessSource;AssessType);\rIndex2=index(\"Value_and_Liquidity\";ExpertRate;BusinessValue);\rif (L*Index1*Index2 > 1) \r      then {RfC2=1;}\r      else {RfC2=L*Index1*Index2;}\rreturn RfC2",
            "CollValueEAD=CollValueLGD*100/EAD;\rreturn CollValueEAD;\r",
            "CurrentOutstanding = Debt;\rInterest = Rate/100;\rEAD=CurrentOutstanding+(Limit-CurrentOutstanding)/2;\rEAD=EAD+EAD*Interest/4;\rreturn EAD",
            "ELMultiplier=1;\rEL=PD*EAD*LGD*ELMultiplier/10000;\rreturn EL",
//            "2K [ReturnEAD=AdjProbDebit=AdjProbDebit=AdjProbReal=TotalRecoveryRec=TotalRecoveryDebit=EADCollatRet=TotalRecReal=LGD=left=NULL;\rTotal_return_from_Coll=0;\rif (isDefined(Return_from_Collateral))\r      then {Total_return_from_Coll = Sigma(Return_from_Collateral);}\r\rif (EAD!=0) \r      then {ReturnEAD = Total_return_from_Coll/EAD;}\rBaseProbX=\"Recovery\";\rBaseProbY=\"Base_Probability\";\rBaseProbability=index(\"Probability_Calculations\"; BaseProbY; BaseProbX);\rBetaX=\"Recovery\";\rBetaY=\"Value\";\rBetaRecovery=index(\"Beta_factor\"; BetaY; BetaX);\rAdjProbRec=min(1; BaseProbability+ReturnEAD*BetaRecovery);\r\rBaseProbX = \"Debit\";\rBaseProbY = \"Base_Probability\";\rBaseProbability=index(\"Probability_Calculations\";BaseProbY;BaseProbX);\rBetaX = \"Debit\";\rBetaY = \"Value\";\rBetaDebit=index(\"Beta_factor\";BetaY;BetaX);\rif (ReturnEAD!=NULL) \r   then {AdjProbDebit=max(0;BaseProbability+ReturnEAD*BetaDebit);}\rif (isDefined(AdjProbDebit)&&isDefined(AdjProbRec)) \r   then {AdjProbReal=1-AdjProbDebit-AdjProbRec;}\rRateofRecoveryY=\"Rate\";\rRateofRecovery...]",
            "ProbDef=index(\"Rating_PD_conversion\";\"PD\";Rating);\rreturn ProbDef*100",
            "if (isDefined(Limit)) then {   if(LoanMode!=\"Nonrevolving_CL\"&&LoanMode!=\"Revolving_CL\"&&LoanMode!=\"Overdraft\"&&LoanMode!=\"Nonrevolving_CL_GA\"&&LoanMode!=\"Revolving_CL_GA\") then {\r   fail (1;\"Для заданного режима кредитования значение 'Лимит/сумма кредита' должно быть пусто\");}}\r    else {if(LoanMode==\"Nonrevolving_CL\"||LoanMode==\"Revolving_CL\"||LoanMode==\"Overdraft\"||LoanMode==\"Nonrevolving_CL_GA\"||LoanMode==\"Revolving_CL_GA\") then {\rfail(1;\"Для заданного режима кредитования значение 'Лимит/сумма кредита' должно быть задано\");}}\rreturn NULL",
            "A1=index(\"Value_and_Liquidity\";ExpertRate;BusinessValue);\rA2=index(\"Type_and_Source_of_Appraisal\";AssessSource;AssessType);\rLimitLogicY = \"Return\";\rB=index(\"Limit_Logic\";LimitLogicY;CollType); \rX=A1*A2*B ;\rReturnRate= min(1;X);\rreturn ReturnRate*100;\r",
            "RfC2=NULL;\rVozvrat = \"Return\";\rTempRating=PDRate;\rM=max(0;0,05*(19-TempRating));\rTempRec=index(\"Limit_Logic\";Vozvrat;CollType);\rIndex1=index(\"Type_and_Source_of_Appraisal\";AssessSource;AssessType);\rIndex2=index(\"Value_and_Liquidity\";ExpertRate;BusinessValue);\rif (TempRec*Index1*Index2 > 1) \r      then {RfC2=1;}\r      else {RfC2=TempRec*Index1*Index2;} \rreturn RfC2\r",
            "RfC2=0;\rVozvrat = \"Return\";\rIndex1=index(\"Type_and_Source_of_Appraisal\";AssessSource;AssessType);\rIndex2=index(\"Value_and_Liquidity\";ExpertRate;BusinessValue);\rTempRec=index(\"Limit_Logic\";Vozvrat;CollType);\rif (TempRec*Index1*Index2 > 1) \r      then {RfC2=1;}\r      else {RfC2=TempRec*Index1*Index2;} \rreturn RfC2",
            "RfC2=TempRating=M=0;\rL=min(0,2;M);\rIndex1=index(\"Type_and_Source_of_Appraisal\";AssessSource;AssessType);\rIndex2=index(\"Value_and_Liquidity\";ExpertRate;BusinessValue);\rif (L*Index1*Index2 > 1) \r      then {RfC2=1;}\r      else {RfC2=L*Index1*Index2;}\rreturn RfC2",
            "return NULL",
            "EL_Percent=EL*100/EAD;\rreturn EL_Percent",
            "Return_from_Collateral=RfC1*RfC2;\rreturn Return_from_Collateral",
            "return 0",
            "return EAD",
            "RfC2=NULL;\rTempRating=PDRate;\rM=max(0;0,05*(19-TempRating));\rL=min(0,8;0,1+M);\rIndex1=index(\"Type_and_Source_of_Appraisal\";AssessSource;AssessType);\rIndex2=index(\"Value_and_Liquidity\";ExpertRate;BusinessValue);\rif (L*Index1*Index2 > 1) \r      then {RfC2=1;}\r      else {RfC2=L*Index1*Index2;}\rreturn RfC2\r",
            "TotalValue = Sigma(AssessmentOut);\rreturn TotalValue",
            "Y=\"Adjustment\";\rX=\"30\";\rELMultiplier=index(\"EL_Adjustment_Days_to_Maturity\";Y;X);\rEL=PD*EAD*LGD*ELMultiplier/10000;\rreturn EL",
            "if (PDRate<=0||PDRate>=27) then {\r   fail(1;\"Значение рейтинга поручителя должно быть в диапазоне от 1 до 26\");}\rreturn NULL",
            "if (isDefined(Rating)) then {\r  if (Rating<=0||Rating>=27) then {\r      fail(1;\"Значение рейтинга должно быть в диапазоне от 1 до 26\");}}\rreturn NULL",
            "A1=index(\"Value_and_Liquidity\";ExpertRate;BusinessValue);\rA2=index(\"Type_and_Source_of_Appraisal\";AssessSource;AssessType);\rY=max(0;0,02*(19-PDRate));\rX=min(0,2;Y)*A1*A2;\rReturnRate= min(1;X);\rreturn ReturnRate*100",
            "return Assessment",
            "return Debt",
            "TotalCollValueEAD = Sigma(CollValueEAD);\rreturn TotalCollValueEAD",
            "Y=\"Adjustment\";\rX=\"180\";\rELMultiplier=index(\"EL_Adjustment_Days_to_Maturity\";Y;X);\rEL=PD*EAD*LGD*ELMultiplier/10000;\rreturn EL",
            "Limit_Logic=\"Limit_Logic\";\rRegime=index(\"Regime_Limit_Logic\";Limit_Logic;LoanMode);\r\rreturn Regime",
            "daysToMaturity=NULL;\rif (isDefined(AgrEndDate)==0)\r   then {daysToMaturity=180;}\r   else {daysToMaturity=delta(AgrEndDate;reportDate);\r         if (daysToMaturity < 0) then {\r         fail(1;\"Дата завершения договора не может быть меньше текущей даты\");}}\r\rreturn daysToMaturity;",
            "if (isDefined(Debt)==0) then {\rfail(1;\"Для данного режима кредитования не указан 'Общий размер ссудоной задолженности'\");};\rCurrentOutstanding = Debt;\rInterest = Rate/100;\rEAD=CurrentOutstanding+CurrentOutstanding*Interest/4;\rreturn EAD",
            "if (isDefined(Debt)==0) then {\rfail(1;\"Для данного режима кредитования не указан 'Общий размер ссудоной задолженности'\");};\rCurrentOutstanding = Debt;\rInterest = Rate/100;\rEAD=CurrentOutstanding+(Limit-CurrentOutstanding)/2;\rEAD=EAD+EAD*Interest/4;\rreturn EAD"
    };

    @Theory
    public void testRecognition(String input) throws Exception {
        Node<Object> node = see.parseReturnExpression(input);
        assertNotNull(node);
    }
}
