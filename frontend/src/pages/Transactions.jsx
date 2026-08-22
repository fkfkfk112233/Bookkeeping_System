import { useCallback, useEffect, useState } from "react";
import TransactionTable from "../components/TransactionTable";
import TransactionModal from "../components/TransactionModal";
import {
  getTransactions,
  createTransaction,
  updateTransaction,
  deleteTransaction,
} from "../services/transactionApi";
import { getCategories } from "../services/categoryApi";

const formatDate = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
};

const getDateRange = (dateRange) => {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, "0");
  const day = String(now.getDate()).padStart(2, "0");

  if (dateRange === "today") {
    const date = `${year}-${month}-${day}`;

    return {
      startDate: date,
      endDate: date,
    };
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

  const firstDay = `${year}-${month}-01`;
  const lastDay = new Date(year, now.getMonth() + 1, 0);

  return {
    startDate: firstDay,
    endDate: formatDate(lastDay),
  };
};

function Transactions() {
  // =========================
  // State
  // =========================

  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState("EXPENSE");
  const [editingTransaction, setEditingTransaction] = useState(null);

  const [categories, setCategories] = useState([]);
  const [transactions, setTransactions] = useState([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [dateRange, setDateRange] = useState("month");

  // =========================
  // Fetch Transactions
  // =========================

  const fetchTransactions = useCallback(async () => {
    try {
      setLoading(true);
      setError("");

      const { startDate, endDate } = getDateRange(dateRange);

      const response = await getTransactions(startDate, endDate);

      setTransactions(response.data);
    } catch (error) {
      console.error("Failed to fetch transactions:", error);

      setError(
        error.response?.data?.message || "無法取得交易紀錄",
      );
    } finally {
      setLoading(false);
    }
  }, [dateRange]);

  // =========================
  // Fetch Categories
  // =========================

  const fetchCategories = async () => {
    try {
      const response = await getCategories();

      setCategories(response.data);
    } catch (error) {
      console.error("Failed to fetch categories:", error);

      setError(
        error.response?.data?.message || "無法取得分類",
      );
    }
  };

  // =========================
  // Transaction Handlers
  // =========================

  const handleEdit = (transaction) => {
    setEditingTransaction(transaction);
    setModalType(transaction.type);
    setShowModal(true);
  };

  const handleDelete = async (transaction) => {
    const confirmed = window.confirm(
      `確定要刪除「${transaction.description || "這筆交易"}」嗎？`,
    );

    if (!confirmed) {
      return;
    }

    try {
      await deleteTransaction(transaction.id);

      await fetchTransactions();
    } catch (error) {
      console.error("Failed to delete transaction:", error);

      setError(
        error.response?.data?.message || "刪除交易失敗",
      );
    }
  };

  const handleAddIncome = () => {
    setEditingTransaction(null);
    setModalType("INCOME");
    setShowModal(true);
  };

  const handleAddExpense = () => {
    setEditingTransaction(null);
    setModalType("EXPENSE");
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setEditingTransaction(null);
  };

  const handleSubmit = async (data) => {
    try {
      if (editingTransaction) {
        await updateTransaction(editingTransaction.id, {
          ...data,
          type: modalType,
        });
      } else {
        await createTransaction({
          ...data,
          type: modalType,
        });
      }

      handleCloseModal();

      await fetchTransactions();
    } catch (error) {
      console.error("Failed to save transaction:", error);

      const message =
        error.response?.data?.message ||
        (editingTransaction
          ? "修改交易失敗"
          : "新增交易失敗");

      setError(message);
    }
  };

  // =========================
  // Filter Transactions
  // =========================

  const incomeTransactions = transactions.filter(
    (transaction) => transaction.type === "INCOME",
  );

  const expenseTransactions = transactions.filter(
    (transaction) => transaction.type === "EXPENSE",
  );

  // =========================
  // Effects
  // =========================

  useEffect(() => {
    fetchTransactions();
  }, [fetchTransactions]);

  useEffect(() => {
    fetchCategories();
  }, []);

  // =========================
  // Render
  // =========================

  return (
    <div className="container-fluid px-4 py-4 page-container">
      <div className="page-header">
        <h1 className="mb-1">記帳</h1>

        {loading && (
          <div className="alert alert-info">
            載入交易紀錄中...
          </div>
        )}

        {error && (
          <div className="alert alert-danger">
            {error}
          </div>
        )}

        <p className="text-body-secondary mb-0">
          管理你的收入與支出
        </p>
      </div>

      <div className="card shadow-sm">
        <div className="card-body">

          {/* 日期範圍 */}
          <div className="btn-group mb-4">
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
          </div>

          {/* 收入交易 */}
          <div className="card shadow-sm mb-4">
            <div className="card-header">
              <h5 className="mb-0">收入</h5>
            </div>

            <div className="card-body p-0">
              <TransactionTable
                transactions={incomeTransactions}
                onEdit={handleEdit}
                onDelete={handleDelete}
              />
            </div>
          </div>

          {/* 支出交易 */}
          <div className="card shadow-sm">
            <div className="card-header">
              <h5 className="mb-0">支出</h5>
            </div>

            <div className="card-body p-0">
              <TransactionTable
                transactions={expenseTransactions}
                onEdit={handleEdit}
                onDelete={handleDelete}
              />
            </div>
          </div>

          {/* 新增按鈕 */}
          <div className="d-flex justify-content-end gap-2 mt-4">
            <button
              type="button"
              className="btn btn-success"
              onClick={handleAddIncome}
            >
              ＋ 新增收入
            </button>

            <button
              type="button"
              className="btn btn-danger"
              onClick={handleAddExpense}
            >
              ＋ 新增支出
            </button>
          </div>

        </div>
      </div>

      {/* Modal */}
      <TransactionModal
        show={showModal}
        type={modalType}
        transaction={editingTransaction}
        categories={categories}
        onClose={handleCloseModal}
        onSubmit={handleSubmit}
      />
    </div>
  );
}

export default Transactions;