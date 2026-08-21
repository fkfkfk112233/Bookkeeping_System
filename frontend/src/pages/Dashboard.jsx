import DateTimeCard from "../components/DateTimeCard";
import SummaryCard from "../components/SummaryCard";
import ChartCard from "../components/ChartCard";
import IncomeExpensePieChart from "../components/IncomeExpensePieChart";
import TrendLineChart from "../components/TrendLineChart";

const expenseData = [
  {
    categoryId: 1,
    categoryName: "飲食",
    amount: 5000,
  },
  {
    categoryId: 2,
    categoryName: "交通",
    amount: 2000,
  },
  {
    categoryId: 3,
    categoryName: "娛樂",
    amount: 1500,
  },
];

const incomeData = [
  {
    categoryId: 4,
    categoryName: "薪資",
    amount: 30000,
  },
  {
    categoryId: 5,
    categoryName: "獎金",
    amount: 5000,
  },
];

const incomeTrendData = [
  {
    label: "08/01",
    amount: 30000,
  },
  {
    label: "08/02",
    amount: 0,
  },
  {
    label: "08/03",
    amount: 5000,
  },
  {
    label: "08/04",
    amount: 0,
  },
  {
    label: "08/05",
    amount: 2000,
  },
];

const expenseTrendData = [
  {
    label: "08/01",
    amount: 1200,
  },
  {
    label: "08/02",
    amount: 500,
  },
  {
    label: "08/03",
    amount: 2500,
  },
  {
    label: "08/04",
    amount: 800,
  },
  {
    label: "08/05",
    amount: 1500,
  },
];

const frequencyData = [
  {
    label: "08/01",
    count: 3,
  },
  {
    label: "08/02",
    count: 5,
  },
  {
    label: "08/03",
    count: 2,
  },
  {
    label: "08/04",
    count: 8,
  },
  {
    label: "08/05",
    count: 4,
  },
];

function Dashboard() {
  return (
    <div className="container-fluid px-4 py-4">
      <div className="mb-4">
        <h1 className="mb-1">Dashboard</h1>

        <p className="text-body-secondary mb-0">個人財務概況</p>
      </div>

      {/* Summary Cards */}

      <div className="row g-4 mb-4">
        <div className="col-12 col-md-6 col-xl-3">
          <DateTimeCard />
        </div>

        <div className="col-12 col-md-6 col-xl-3">
          <SummaryCard title="本月收入" value="$0" />
        </div>

        <div className="col-12 col-md-6 col-xl-3">
          <SummaryCard title="本月支出" value="$0" />
        </div>

        <div className="col-12 col-md-6 col-xl-3">
          <SummaryCard title="本月結餘" value="$0" />
        </div>
      </div>

      {/* Date Range */}

      <div className="card shadow-sm mb-4">
        <div className="card-body">
          <div className="btn-group">
            <button type="button" className="btn btn-primary">
              今日
            </button>

            <button type="button" className="btn btn-outline-primary">
              本週
            </button>

            <button type="button" className="btn btn-outline-primary">
              本月
            </button>

            <button type="button" className="btn btn-outline-primary">
              本年
            </button>
          </div>
        </div>
      </div>

      {/* Charts */}

      <div className="row g-4">
        <div className="col-12 col-xl-6">
          <ChartCard title="收入分類">
            <IncomeExpensePieChart data={incomeData} />
          </ChartCard>
        </div>

        <div className="col-12 col-xl-6">
          <ChartCard title="支出分類">
            <IncomeExpensePieChart data={expenseData} />
          </ChartCard>
        </div>

        <div className="col-12 col-xl-6">
          <div className="card shadow-sm">
            <div className="card-body">
              <h5 className="card-title">收入趨勢</h5>

              <TrendLineChart
                data={incomeTrendData}
                label="收入"
                valueKey="amount"
              />
            </div>
          </div>
        </div>

        <div className="col-12 col-xl-6">
          <div className="card shadow-sm">
            <div className="card-body">
              <h5 className="card-title">支出趨勢</h5>

              <TrendLineChart
                data={expenseTrendData}
                label="支出"
                valueKey="amount"
              />
            </div>
          </div>
        </div>

        <div className="col-12">
          <div className="card shadow-sm">
            <div className="card-body">
              <ChartCard title="使用頻率">
                <TrendLineChart
                  data={frequencyData}
                  label="記帳次數"
                  valueKey="count"
                />
              </ChartCard>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
