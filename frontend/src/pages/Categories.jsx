import { useEffect, useState } from "react";
import { getCategories, createCategory } from "../services/categoryApi";

function Categories() {
  const [categories, setCategories] = useState([]);
  const [error, setError] = useState("");

  // 新增分類 Modal
  const [showModal, setShowModal] = useState(false);

  const [categoryName, setCategoryName] = useState("");
  const [categoryType, setCategoryType] = useState("EXPENSE");

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await getCategories();
        setCategories(response.data);
      } catch (error) {
        console.error("取得分類失敗:", error);
        setError("取得分類失敗");
      }
    };

    fetchCategories();
  }, []);

  const handleCreateCategory = async () => {
    try {
      await createCategory({
        name: categoryName,
        type: categoryType,
      });

      // 重新取得分類
      const response = await getCategories();
      setCategories(response.data);

      // 清空表單
      setCategoryName("");
      setCategoryType("EXPENSE");

      // 關閉 Modal
      setShowModal(false);
    } catch (error) {
      console.error("新增分類失敗:", error);
      setError("新增分類失敗");
    }
  };

  const incomeCategories = categories.filter(
    (category) => category.type === "INCOME",
  );

  const expenseCategories = categories.filter(
    (category) => category.type === "EXPENSE",
  );

  const renderCategoryTable = (categoryList, typeText) => {
    return (
      <div className="card">
        <div className="card-header">
          <h5 className="mb-0">{typeText}分類</h5>
        </div>

        <div className="card-body p-0">
          <table className="table table-striped table-hover mb-0">
            <thead>
              <tr>
                <th>ID</th>
                <th>分類名稱</th>
                <th>類型</th>
              </tr>
            </thead>

            <tbody>
              {categoryList.map((category, index) => (
                <tr key={category.id}>
                  <td>{index + 1}</td>
                  <td>{category.name}</td>
                  <td>{typeText}</td>
                </tr>
              ))}

              {categoryList.length === 0 && (
                <tr>
                  <td colSpan="3" className="text-center text-muted">
                    尚無分類
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    );
  };

  return (
    <div className="container mt-4">
      {/* 標題 + 新增按鈕 */}
      <div className="d-flex justify-content-between align-items-center">
        <h1 className="mb-0">分類管理</h1>

        <button
          type="button"
          className="btn btn-primary"
          onClick={() => setShowModal(true)}
        >
          ＋ 新增分類
        </button>
      </div>

      {error && <div className="alert alert-danger mt-3">{error}</div>}

      {/* 收入分類 */}
      <div className="mt-4">
        {renderCategoryTable(incomeCategories, "收入")}
      </div>

      {/* 支出分類 */}
      <div className="mt-4">
        {renderCategoryTable(expenseCategories, "支出")}
      </div>

      {/* 新增分類 Modal */}
      {showModal && (
        <div
          className="modal fade show d-block"
          tabIndex="-1"
          role="dialog"
          style={{ backgroundColor: "rgba(0, 0, 0, 0.5)" }}
        >
          <div className="modal-dialog" role="document">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">新增分類</h5>

                <button
                  type="button"
                  className="btn-close"
                  onClick={() => setShowModal(false)}
                ></button>
              </div>

              <div className="modal-body">
                <div className="mb-3">
                  <label htmlFor="categoryName" className="form-label">
                    分類名稱
                  </label>

                  <input
                    type="text"
                    className="form-control"
                    id="categoryName"
                    value={categoryName}
                    onChange={(e) => setCategoryName(e.target.value)}
                    placeholder="請輸入分類名稱"
                  />
                </div>

                <div className="mb-3">
                  <label htmlFor="categoryType" className="form-label">
                    類型
                  </label>

                  <select
                    className="form-select"
                    id="categoryType"
                    value={categoryType}
                    onChange={(e) => setCategoryType(e.target.value)}
                  >
                    <option value="INCOME">收入</option>
                    <option value="EXPENSE">支出</option>
                  </select>
                </div>
              </div>

              <div className="modal-footer">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => setShowModal(false)}
                >
                  取消
                </button>

                <button
                  type="button"
                  className="btn btn-primary"
                  onClick={handleCreateCategory}
                >
                  新增
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Categories;
