import { useEffect, useState } from "react";

function getCurrentDateTime() {
  const now = new Date();

  const year = now.getFullYear();

  const month = String(now.getMonth() + 1).padStart(2, "0");

  const day = String(now.getDate()).padStart(2, "0");

  const hours = String(now.getHours()).padStart(2, "0");

  const minutes = String(now.getMinutes()).padStart(2, "0");

  return `${year}-${month}-${day}T${hours}:${minutes}`;
}

function TransactionModal({
  show,
  type,
  transaction,
  categories,
  onClose,
  onSubmit,
}) {
  const [categoryId, setCategoryId] = useState("");
  const [amount, setAmount] = useState("");
  const [paymentMethod, setPaymentMethod] = useState("CASH");
  const [transactionDate, setTransactionDate] = useState("");
  const [description, setDescription] = useState("");

  useEffect(() => {
    if (transaction) {
      setCategoryId(transaction.categoryId ?? "");

      setAmount(transaction.amount ?? "");

      setPaymentMethod(transaction.paymentMethod ?? "CASH");

      setTransactionDate(
        transaction.transactionDate
          ? transaction.transactionDate.slice(0, 16)
          : getCurrentDateTime(),
      );

      setDescription(transaction.description ?? "");
    } else {
      setCategoryId("");
      setAmount("");
      setPaymentMethod("CASH");
      setTransactionDate(getCurrentDateTime());
      setDescription("");
    }
  }, [transaction, show]);

  const handleSubmit = (event) => {
    event.preventDefault();

    if (!categoryId) {
      return;
    }

    const numericAmount = Number(amount);

    if (!Number.isFinite(numericAmount) || numericAmount < 10 || numericAmount % 10 !== 0) {
      return;
    }

    if (!transactionDate) {
      return;
    }

    onSubmit({
      categoryId: Number(categoryId),
      amount: numericAmount,
      paymentMethod,
      transactionDate,
      description,
    });
  };

  const filteredCategories = categories.filter(
    (category) => category.type === type,
  );

  return (
    <div
      className={`modal fade ${show ? "show d-block" : ""}`}
      tabIndex="-1"
      role="dialog"
      style={{
        backgroundColor: show ? "rgba(0, 0, 0, 0.5)" : "transparent",
      }}
    >
      <div className="modal-dialog modal-dialog-centered">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">
              {transaction
                ? "編輯交易"
                : type === "INCOME"
                  ? "新增收入"
                  : "新增支出"}
            </h5>

            <button type="button" className="btn-close" onClick={onClose} />
          </div>

          <form onSubmit={handleSubmit}>
            <div className="modal-body">
              {/* Category */}

              <div className="mb-3">
                <label className="form-label">類別</label>

                <select
                  className="form-select"
                  value={categoryId}
                  onChange={(event) => setCategoryId(event.target.value)}
                  required
                >
                  <option value="">請選擇類別</option>

                  {filteredCategories.map((category) => (
                    <option key={category.id} value={category.id}>
                      {category.name}
                    </option>
                  ))}
                </select>
              </div>

              {/* Amount */}

              <div className="mb-3">
                <label className="form-label">金額</label>

                <input
                  type="number"
                  className="form-control"
                  value={amount}
                  onChange={(event) => setAmount(event.target.value)}
                  min="10"
                  step="10"
                  required
                />
              </div>

              {/* Payment Method */}

              <div className="mb-3">
                <label className="form-label">支付方式</label>

                <select
                  className="form-select"
                  value={paymentMethod}
                  onChange={(event) => setPaymentMethod(event.target.value)}
                >
                  <option value="CASH">現金</option>

                  <option value="CREDIT_CARD">信用卡</option>
                </select>
              </div>

              {/* Date */}

              <div className="mb-3">
                <label className="form-label">日期</label>

                <input
                  type="datetime-local"
                  className="form-control"
                  value={transactionDate}
                  onChange={(event) => setTransactionDate(event.target.value)}
                  required
                />
              </div>

              {/* Description */}

              <div className="mb-3">
                <label className="form-label">備註</label>

                <textarea
                  className="form-control"
                  rows="3"
                  value={description}
                  onChange={(event) => setDescription(event.target.value)}
                />
              </div>
            </div>

            <div className="modal-footer">
              <button
                type="button"
                className="btn btn-secondary"
                onClick={onClose}
              >
                取消
              </button>

              <button type="submit" className="btn btn-primary">
                儲存
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default TransactionModal;
