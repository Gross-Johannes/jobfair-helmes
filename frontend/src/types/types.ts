export type ApiResponse<T> = {
  message: string;
  data: T;
  errors: string[] | null;
};

export type CalculationResponse = ApiResponse<CalculationData>;
export type BenefitResponse = ApiResponse<BenefitData>;

export type CalculationData = {
  monthlyEligibleSalary: number;
  dailyRate: number;
  totalPayment: number;
  breakdown: MonthlyBreakdownItem[];
};

export type BenefitData = {
  id: string;
  grossSalary: number;
  babyBirthDate: string;
  calculationResult: CalculationData;
};

export type MonthlyBreakdownItem = {
  year: number;
  month: number;
  payableDays: number;
  paymentAmount: number;
};

export type CalculationResultProps = CalculationResponse & {
  grossSalary: number;
  babyBirthDate: string;
};

export type RequestInput = {
  grossSalary: number;
  babyBirthDate: string;
};
