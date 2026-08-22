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

const formatDate = (date) => {
  const year = date.getFullYear();

  const month = String(date.getMonth() + 1).padStart(2, "0");

  const day = String(date.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
};

const getDateRange = (dateRange) => {
  const now = new Date();
  const year = now.getFullYear();
  const month = now.getMonth();

  if (dateRange === "today") {
    const date = formatDate(now);
    return { startDate: date, endDate: date };
  }

  if (dateRange === "week") {
    const currentDay = now.getDay();
    const diff = currentDay === 0 ? -6 : 1 - currentDay;
    const start = new Date(now);
    start.setDate(now.getDate() + diff);
    const end = new Date(start);
    end.setDate(start.getDate() + 6);

    return {
      startDate: formatDate(start),
      endDate: formatDate(end),
    };
  }

  if (dateRange === "year") {
    return {
      startDate: `${year}-01-01`,
      endDate: `${year}-12-31`,
    };
  }

  const start = new Date(year, month, 1);
  const end = new Date(year, month + 1, 0);

  return {
    startDate: formatDate(start),
    endDate: formatDate(end),
  };
};

function Dashboard() {
  const [summary, setSummary] = useState({
    income: 0,
    expense: 0,
    balance: 0,
  });
  const [loading, setLoading] = useState(true);

  const [error, setError] = useState("");

  const [dateRange, setDateRange] = useState("month");

  const getDateRangeLabel = () => {
    const labels = {
      today: "今日",
      week: "本週",
      month: "本月",
      year: "本年",
    };

    return labels[dateRange] ?? "本月";
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

        const { startDate, endDate } = getDateRange(dateRange);

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
  }, [dateRange]);

  return (
    <div className="container-fluid px-4 py-4 page-container">
      <div className="page-header">
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
            title={`${getDateRangeLabel()}收入`}
            value={`$${summary.income.toLocaleString()}`}
          />
        </div>

        <div className="col-12 col-md-6 col-xl-3">
          <SummaryCard
            title={`${getDateRangeLabel()}支出`}
            value={`$${summary.expense.toLocaleString()}`}
          />
        </div>

        <div className="col-12 col-md-6 col-xl-3">
          <SummaryCard
            title={`${getDateRangeLabel()}結餘`}
            value={`$${summary.balance.toLocaleString()}`}
          />
        </div>
      </div>

      {/* Date Range */}

      <div className="card shadow-sm mb-4">
        <div className="card-body">
          <div className="btn-group">
            <button
              type="button"
              className={
                dateRange === "today"
                  ? "btn btn-primary"
                  : "btn btn-outline-primary"
              }
              onClick={() => setDateRange("today")}
            >
              今日
            </button>

            <button
              type="button"
              className={
                dateRange === "week"
                  ? "btn btn-primary"
                  : "btn btn-outline-primary"
              }
              onClick={() => setDateRange("week")}
            >
              本週
            </button>

            <button
              type="button"
              className={
                dateRange === "month"
                  ? "btn btn-primary"
                  : "btn btn-outline-primary"
              }
              onClick={() => setDateRange("month")}
            >
              本月
            </button>

            <button
              type="button"
              className={
                dateRange === "year"
                  ? "btn btn-primary"
                  : "btn btn-outline-primary"
              }
              onClick={() => setDateRange("year")}
            >
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
