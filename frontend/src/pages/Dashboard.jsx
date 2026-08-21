import DateTimeCard from "../components/DateTimeCard";
import SummaryCard from "../components/SummaryCard";

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
          <div className="card shadow-sm">
            <div className="card-body">
              <h5 className="card-title">收入分類</h5>

              <div className="py-5 text-center text-body-secondary">
                Pie Chart
              </div>
            </div>
          </div>
        </div>

        <div className="col-12 col-xl-6">
          <div className="card shadow-sm">
            <div className="card-body">
              <h5 className="card-title">支出分類</h5>

              <div className="py-5 text-center text-body-secondary">
                Pie Chart
              </div>
            </div>
          </div>
        </div>

        <div className="col-12 col-xl-6">
          <div className="card shadow-sm">
            <div className="card-body">
              <h5 className="card-title">收入趨勢</h5>

              <div className="py-5 text-center text-body-secondary">
                Line Chart
              </div>
            </div>
          </div>
        </div>

        <div className="col-12 col-xl-6">
          <div className="card shadow-sm">
            <div className="card-body">
              <h5 className="card-title">支出趨勢</h5>

              <div className="py-5 text-center text-body-secondary">
                Line Chart
              </div>
            </div>
          </div>
        </div>

        <div className="col-12">
          <div className="card shadow-sm">
            <div className="card-body">
              <h5 className="card-title">使用頻率</h5>

              <div className="py-5 text-center text-body-secondary">
                Line Chart
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
