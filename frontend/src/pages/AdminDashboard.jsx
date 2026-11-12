import { useEffect, useState } from "react";
import { getAdminDashboardSummary } from "../services/dashboardApi";

const statCards = [
  {
    key: "adminCount",
    title: "管理者人數",
    description: "目前管理者帳號",
    icon: "♜",
    color: "admin-stat-purple",
  },
  {
    key: "userCount",
    title: "一般使用者人數",
    description: "一般使用者帳號",
    icon: "♧",
    color: "admin-stat-blue",
  },
  {
    key: "todayLoginCount",
    title: "今日登入人數",
    description: "今日曾成功登入的帳號",
    icon: "↗",
    color: "admin-stat-teal",
  },
  {
    key: "disabledUserCount",
    title: "停權人數",
    description: "目前已停用的帳號",
    icon: "⊘",
    color: "admin-stat-orange",
  },
];

function AdminDashboard() {
  const [summary, setSummary] = useState({
    adminCount: 0,
    userCount: 0,
    todayLoginCount: 0,
    disabledUserCount: 0,
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let active = true;

    const fetchSummary = async () => {
      try {
        setLoading(true);
        setError("");
        const response = await getAdminDashboardSummary();
        if (active) {
          setSummary(response.data);
        }
      } catch (requestError) {
        console.error("取得管理者 Dashboard 失敗:", requestError);
        if (active) {
          setError("無法取得管理者統計資料，請確認管理者登入狀態及後端服務。");
        }
      } finally {
        if (active) {
          setLoading(false);
        }
      }
    };

    fetchSummary();
    return () => {
      active = false;
    };
  }, []);

  return (
    <div className="container-fluid px-4 py-4 page-container">
      <div className="page-header">
        <h1 className="mb-1">Admin Dashboard</h1>
        <p className="text-body-secondary mb-0">系統使用者概況</p>
      </div>

      {loading && <div className="alert alert-info">載入管理者統計資料中...</div>}
      {error && <div className="alert alert-danger" role="alert">{error}</div>}

      <div className="row g-4">
        {statCards.map((card) => (
          <div className="col-12 col-sm-6 col-xl-3" key={card.key}>
            <div className={`card admin-stat-card h-100 ${card.color}`}>
              <div className="card-body d-flex justify-content-between align-items-start">
                <div>
                  <p className="admin-stat-title mb-2">{card.title}</p>
                  <h2 className="admin-stat-value mb-2">
                    {loading ? "—" : Number(summary[card.key] ?? 0).toLocaleString("zh-TW")}
                  </h2>
                  <p className="admin-stat-description mb-0">{card.description}</p>
                </div>
                <span className="admin-stat-icon" aria-hidden="true">
                  {card.icon}
                </span>
              </div>
            </div>
          </div>
        ))}
      </div>

      <div className="card admin-welcome-card mt-4">
        <div className="card-body">
          <h5 className="mb-2">系統管理</h5>
          <p className="text-body-secondary mb-0">
            你可以從上方查看帳號統計，並透過「User Management」管理系統使用者。
          </p>
        </div>
      </div>
    </div>
  );
}

export default AdminDashboard;
