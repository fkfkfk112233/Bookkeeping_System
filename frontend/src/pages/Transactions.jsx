import { useState } from "react";
import TransactionTable from "../components/TransactionTable";

function Transactions() {
  const [transactions] = useState([
    {
      id: 1,
      type: "EXPENSE",
      categoryName: "飲食",
      amount: 120,
      paymentMethod: "CASH",
      description: "午餐",
      transactionDate: "2026-08-21",
    },
    {
      id: 2,
      type: "EXPENSE",
      categoryName: "交通",
      amount: 50,
      paymentMethod: "CASH",
      description: "捷運",
      transactionDate: "2026-08-21",
    },
    {
      id: 3,
      type: "INCOME",
      categoryName: "薪資",
      amount: 30000,
      paymentMethod: "CASH",
      description: "月薪",
      transactionDate: "2026-08-21",
    },
  ]);

  const handleEdit = (transaction) => {
    console.log("Edit:", transaction);
  };

  const handleDelete = (transaction) => {
    console.log("Delete:", transaction);
  };

  return (
    <div className="container-fluid px-4 py-4">
      <div className="mb-4">
        <h1 className="mb-1">記帳</h1>

        <p className="text-body-secondary mb-0">管理你的收入與支出</p>
      </div>

      <div className="card shadow-sm">
        <div className="card-body">
          <div className="btn-group mb-4">
            <button type="button" className="btn btn-primary">
              今日
            </button>

            <button type="button" className="btn btn-outline-primary">
              本週
            </button>

            <button type="button" className="btn btn-outline-primary">
              本月
            </button>
          </div>

          <TransactionTable
            transactions={transactions}
            onEdit={handleEdit}
            onDelete={handleDelete}
          />

          <div className="d-flex justify-content-end gap-2 mt-4">
            <button type="button" className="btn btn-success">
              ＋ 新增收入
            </button>

            <button type="button" className="btn btn-danger">
              ＋ 新增支出
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Transactions;
