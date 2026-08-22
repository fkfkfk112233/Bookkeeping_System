function TransactionTable({ transactions, onEdit, onDelete }) {
  return (
    <div className="table-responsive">
      <table className="table table-hover align-middle mb-0 text-nowrap">
        <thead>
          <tr>
            <th>編號</th>
            <th>類型</th>
            <th>類別</th>
            <th>金額</th>
            <th>支付方式</th>
            <th>備註</th>
            <th>日期</th>
            <th className="text-end">操作</th>
          </tr>
        </thead>

        <tbody>
          {transactions.length === 0 ? (
            <tr>
              <td colSpan="8" className="text-center py-5 text-body-secondary">
                目前沒有交易紀錄
              </td>
            </tr>
          ) : (
            transactions.map((transaction, index) => (
              <tr key={transaction.id}>
                <td>{index + 1}</td>
                <td>{transaction.type === "INCOME" ? "收入" : "支出"}</td>

                <td>{transaction.categoryName ?? "未分類"}</td>

                <td>${Number(transaction.amount).toLocaleString()}</td>

                <td>
                  {transaction.paymentMethod === "CASH" ? "現金" : "信用卡"}
                </td>

                <td>{transaction.description || "-"}</td>

                <td>{transaction.transactionDate}</td>

                <td className="text-end">
                  <button
                    type="button"
                    className="btn btn-sm btn-outline-primary me-2"
                    onClick={() => onEdit(transaction)}
                  >
                    編輯
                  </button>

                  <button
                    type="button"
                    className="btn btn-sm btn-outline-danger"
                    onClick={() => onDelete(transaction)}
                  >
                    刪除
                  </button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default TransactionTable;
