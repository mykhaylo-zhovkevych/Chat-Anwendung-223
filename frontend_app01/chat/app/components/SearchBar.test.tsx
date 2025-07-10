import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import axios from 'axios';
import SearchBar from '../components/SearchBar'; 

jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe('SearchBar', () => {
  test('renders input and button', () => {
    render(<SearchBar onUserSelect={jest.fn()} />);
    expect(screen.getByPlaceholderText(/search username/i)).toBeInTheDocument();
    expect(screen.getByRole('button')).toBeInTheDocument();
  });

  test('shows search results after successful API call and calls onUserSelect on click', async () => {
    const fakeUser = { id: '1', username: 'testuser' };
    mockedAxios.get.mockResolvedValueOnce({ data: fakeUser });

    const onUserSelect = jest.fn();

    render(<SearchBar onUserSelect={onUserSelect} />);

    fireEvent.change(screen.getByPlaceholderText(/search username/i), { target: { value: 'testuser' } });
    fireEvent.click(screen.getByRole('button'));

    await waitFor(() => {
      expect(screen.getByText('testuser')).toBeInTheDocument();
    });

    fireEvent.click(screen.getByText('testuser'));
    expect(onUserSelect).toHaveBeenCalledWith(fakeUser);
  });
});
