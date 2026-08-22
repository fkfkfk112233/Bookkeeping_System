import { useEffect, useState } from "react";
import TransactionTable from "../components/TransactionTable";
import TransactionModal from "../components/TransactionModal";
import {
  getTransactions,
  createTransaction,
  updateTransaction,
} from "../services/transactionApi";
import { getCategories } from "../services/categoryApi";

function Transactions() {
  const [showModal, setShowModal] = useState(false);

  const [modalType, setModalType] = useState("EXPENSE");

  const [editingTransaction, setEditingTransaction] = useState(null);

  const [categories, setCategories] = useState([]);

  const [transactions, setTransactions] = useState([]);

  const handleEdit = (transaction) => {
    setEditingTransaction(transaction);

    setModalType(transaction.type);

    setShowModal(true);
  };

  const [loading, setLoading] = useState(true);

  const [error, setError] = useState("");

  const handleDelete = (transaction) => {
    console.log("Delete:", transaction);
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

      setError(editingTransaction ? "修改交易失敗" : "新增交易失敗");
    }
  };

  const [dateRange, setDateRange] = useState("month");

  const getDateRange = () => {
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

  const formatDate = (date) => {
    const year = date.getFullYear();

    const month = String(date.getMonth() + 1).padStart(2, "0");

    const day = String(date.getDate()).padStart(2, "0");

    return `${year}-${month}-${day}`;
  };

  const fetchTransactions = async () => {
    try {
      setLoading(true);
      setError("");

      const { startDate, endDate } = getDateRange();

      const response = await getTransactions(startDate, endDate);

      setTransactions(response.data);
    } catch (error) {
      console.error("Failed to fetch transactions:", error);

      setError("無法取得交易紀錄");
    } finally {
      setLoading(false);
    }
  };

  const fetchCategories = async () => {
    try {
      const response = await getCategories();

      setCategories(response.data);
    } catch (error) {
      console.error("Failed to fetch categories:", error);

      setError("無法取得分類");
    }
  };

  useEffect(() => {
    fetchTransactions();
  }, [dateRange]);

  useEffect(() => {
    fetchCategories();
  }, []);

  return (
    <div className="container-fluid px-4 py-4">
      <div className="mb-4">
        <h1 className="mb-1">記帳</h1>

        {loading && <div className="alert alert-info">載入交易紀錄中...</div>}
        {error && <div className="alert alert-danger">{error}</div>}

        <p className="text-body-secondary mb-0">管理你的收入與支出</p>
      </div>
      <div className="card shadow-sm">
        <div className="card-body">
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

          <TransactionTable
            transactions={transactions}
            onEdit={handleEdit}
            onDelete={handleDelete}
          />

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
