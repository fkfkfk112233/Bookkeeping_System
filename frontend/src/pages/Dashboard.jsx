import { useEffect, useState } from "react";

import DateTimeCard from "../components/DateTimeCard";
import SummaryCard from "../components/SummaryCard";
import ChartCard from "../components/ChartCard";
import IncomeExpensePieChart from "../components/IncomeExpensePieChart";
import TrendLineChart from "../components/TrendLineChart";
import {
  getDashboardSummary,
  getDashboardIncome,
  getDashboardExpense,
  getDashboardIncomeTrend,
  getDashboardExpenseTrend,
  getDashboardFrequency,
} from "../services/dashboardApi";

function Dashboard() {
  const [summary, setSummary] = useState({
    income: 0,
    expense: 0,
    balance: 0,
  });
  const [loading, setLoading] = useState(true);

  const [error, setError] = useState("");

  const getCurrentMonthRange = () => {
    const now = new Date();

    const year = now.getFullYear();

    const month = String(now.getMonth() + 1).padStart(2, "0");

    const lastDay = new Date(year, now.getMonth() + 1, 0).getDate();

    return {
      startDate: `${year}-${month}-01`,
      endDate: `${year}-${month}-${String(lastDay).padStart(2, "0")}`,
    };
  };

  const [incomeData, setIncomeData] = useState([]);

  const [expenseData, setExpenseData] = useState([]);

  const [incomeTrendData, setIncomeTrendData] = useState([]);

  const [expenseTrendData, setExpenseTrendData] = useState([]);

  const [frequencyData, setFrequencyData] = useState([]);
  useEffect(() => {
    const fetchDashboard = async () => {
      try {
        setLoading(true);
        setError("");

        const { startDate, endDate } = getCurrentMonthRange();

        const [
          summaryResponse,
          incomeResponse,
          expenseResponse,
          incomeTrendResponse,
          expenseTrendResponse,
          frequencyResponse,
        ] = await Promise.all([
          getDashboardSummary(startDate, endDate),

          getDashboardIncome(startDate, endDate),

          getDashboardExpense(startDate, endDate),

          getDashboardIncomeTrend(startDate, endDate),

          getDashboardExpenseTrend(startDate, endDate),

          getDashboardFrequency(startDate, endDate),
        ]);

        setSummary(summaryResponse.data);

        setIncomeData(incomeResponse.data);

        setExpenseData(expenseResponse.data);

        setIncomeTrendData(incomeTrendResponse.data);

        setExpenseTrendData(expenseTrendResponse.data);

        setFrequencyData(frequencyResponse.data);
      } catch (error) {
        console.error("Failed to fetch dashboard:", error);

        setError("無法取得 Dashboard 資料");
      } finally {
        setLoading(false);
      }
    };

    fetchDashboard();
  }, []);

  return (
    <div className="container-fluid px-4 py-4">
      <div className="mb-4">
        <h1 className="mb-1">Dashboard</h1>

        <p className="text-body-secondary mb-0">個人財務概況</p>
      </div>

      {loading && (
        <div className="alert alert-info">載入 Dashboard 資料中...</div>
      )}

      {error && <div className="alert alert-danger">{error}</div>}

      {/* Summary Cards */}

      <div className="row g-4 mb-4">
        <div className="col-12 col-md-6 col-xl-3">
          <DateTimeCard />
        </div>

        <div className="col-12 col-md-6 col-xl-3">
          <SummaryCard
            title="本月收入"
            value={`$${summary.income.toLocaleString()}`}
          />
        </div>

        <div className="col-12 col-md-6 col-xl-3">
          <SummaryCard
            title="本月支出"
            value={`$${summary.expense.toLocaleString()}`}
          />
        </div>

        <div className="col-12 col-md-6 col-xl-3">
          <SummaryCard
            title="本月結餘"
            value={`$${summary.balance.toLocaleString()}`}
          />
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
