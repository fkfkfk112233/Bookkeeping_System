import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { Pie } from "react-chartjs-2";
import { getCategoryColor } from "../utils/categoryPalette";

ChartJS.register(ArcElement, Tooltip, Legend);

function IncomeExpensePieChart({ data = [] }) {
  const chartData = {
    labels: data.map((item) => item.categoryName),
    datasets: [
      {
        data: data.map((item) => item.amount),
        backgroundColor: data.map((item) => getCategoryColor(item.categoryName)),
        borderColor: "#FFFFFF",
        borderWidth: 3,
        hoverOffset: 8,
      },
    ],
  };

  return (
    <div className="chart-container">
      <Pie
        data={chartData}
        options={{
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              position: "bottom",
              labels: { usePointStyle: true, padding: 18 },
            },
          },
        }}
      />
    </div>
  );
}

export default IncomeExpensePieChart;
